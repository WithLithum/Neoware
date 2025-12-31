package x.withlithum.neoware.level.worldgen.generators;

import com.github.kilianB.pcg.fast.PcgRSFast;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import x.withlithum.neoware.level.worldgen.LocalHumidityType;
import x.withlithum.neoware.level.worldgen.biome.NeonBiomes;
import x.withlithum.neoware.util.MathHelper;
import x.withlithum.neoware.util.noise.OpenSimplex2;
import x.withlithum.neoware.util.noise.OpenSimplex2S;

public class SimplexGenerator implements Generator {

    private static final int MIN_TERRAIN_HEIGHT = 61;
    private static final int TREE_HEIGHT = 3;

    private static final double SUBDIVISION_SIZE = 4;

    private static final double MULTIPLY_CARVER_NOISE_BY = 35.5;
    private static final double MULTIPLY_TERRAIN_NOISE_BY = 65;
    private static final double MULTIPLY_SELECTOR_NOISE_BY = 200;
    private static final double MULTIPLY_DRY_SELECTOR_NOISE_BY = 400;
    private static final double MULTIPLY_HUMIDITY_NOISE_BY = 50;

    private static final double LOW_NOISE_HEIGHT_SCALE = 3;
    private static final double HIGH_NOISE_HEIGHT_SCALE = 7;

    private final ThreadLocal<PcgRSFast> pcg = ThreadLocal.withInitial(PcgRSFast::new);
    private final long seed;

    public SimplexGenerator(long seed) {
        this.seed = seed;
    }

    @Contract(pure = true)
    private LocalHumidityType generateHumidity(@NonNull Point pos) {
        var noise = OpenSimplex2.noise2(seed, subdividePos(pos.blockX()) / MULTIPLY_HUMIDITY_NOISE_BY,
            subdividePos(pos.blockZ()) / MULTIPLY_HUMIDITY_NOISE_BY);
        if (noise <= -0.49) {
            return LocalHumidityType.DRY;
        } else if (noise > -0.49 && noise < 0) {
            return LocalHumidityType.NORMAL;
        } else if (noise > 0 && noise <= 0.49) {
            return LocalHumidityType.WET;
        } else {
            return LocalHumidityType.HUMID;
        }
    }

    private boolean isCorrupted(@NonNull Point pos) {
        var noise = OpenSimplex2.noise2(seed, subdividePos(pos.blockX()) / MULTIPLY_HUMIDITY_NOISE_BY,
            subdividePos(pos.blockZ()) / MULTIPLY_HUMIDITY_NOISE_BY);
        return noise < -0.90 || noise > 0.90;
    }

    private static int subdividePos(int blockPos) {
        return (int)Math.floor((double)blockPos / SUBDIVISION_SIZE);
    }

    private static RegistryKey<Biome> getBiomeForHumidity(@NonNull LocalHumidityType humidity) {
        return switch (humidity) {
            case DRY -> NeonBiomes.PHASE_DESERT;
            case NORMAL -> NeonBiomes.BORDERLINE_DRY_LAND;
            case WET -> NeonBiomes.SEASONAL_PHASE_LAND;
            case HUMID -> NeonBiomes.PHASE_WETLANDS;
        };
    }

    @Contract(pure = true)
    private static Block getCoverMaterial(@NonNull LocalHumidityType humidity) {
        return switch (humidity) {
            case DRY -> Block.WHITE_CONCRETE_POWDER;
            case NORMAL -> Block.BROWN_CONCRETE_POWDER;
            case WET, HUMID -> Block.GRASS_BLOCK;
        };
    }

    @Contract(pure = true)
    private static boolean shouldPutGrass(@NonNull Point point, double carverNoise) {
        var xMatter = (int) Math.ceil(point.blockX() + ((carverNoise * 10) / 2)) * 2;
        var zMatter = (int) Math.round(point.blockZ() + ((carverNoise * 7.5) / 0.55)) * 3;

        return xMatter % 7 == 0 && zMatter % 5 == 0;
    }

    private static double selectNoise(double high, double low, double selector) {
        return MathHelper.lerp(high, low, selector);
    }

    @Override
    public void generate(GenerationUnit unit) {
        var start = unit.absoluteStart();
        var end = unit.absoluteEnd();

        var rng = pcg.get();
        var size = unit.size();

        // Heightmaps for placing features
        int[][] floorMap = new int[size.blockX()][size.blockZ()];
        boolean[][] carveMap = new boolean[size.blockX()][size.blockZ()];

        // Log points etc.
        Point logPoint = null;

        unit.modifier().fillHeight(1, 1, Block.BEDROCK);
        unit.modifier().fillHeight(1, MIN_TERRAIN_HEIGHT, Block.STONE);

        rng.setSeed(seed, start.chunkX() + start.chunkZ());

        // Set defaulting biome
        unit.modifier().fillBiome(NeonBiomes.BORDERLINE_DRY_LAND);

        // Fill surface
        for (int x = 0; x < size.blockX(); x++) {
            for (int z = 0; z < size.blockZ(); z++) {
                var currentPos = start.add(x, 0, z);
                var humidity = generateHumidity(currentPos);
                var biome = getBiomeForHumidity(humidity);

                // Use three noise factors to generate terrain noise ;)
                var highNoise = getNoise(currentPos, HIGH_NOISE_HEIGHT_SCALE, 1D, MULTIPLY_TERRAIN_NOISE_BY);
                var lowNoise = getNoise(currentPos, LOW_NOISE_HEIGHT_SCALE, 1D, MULTIPLY_TERRAIN_NOISE_BY);
                var selectorNoise = getNoise(currentPos, 1, 0D, humidity == LocalHumidityType.DRY
                    ? MULTIPLY_DRY_SELECTOR_NOISE_BY
                    : MULTIPLY_SELECTOR_NOISE_BY);
                var heightNoise = selectNoise(highNoise, lowNoise, selectorNoise);

                var floorY = MIN_TERRAIN_HEIGHT + (int) heightNoise;
                var tileY = floorY - 1;
                var floorPos = currentPos.withY(floorY);
                var tilePos = currentPos.withY(tileY);
                var underlyingMaterial = Block.DIRT;
                var coverMaterial = getCoverMaterial(humidity);

                if (isCorrupted(currentPos)) {
                    biome = NeonBiomes.CORRUPTED;
                    var purple = rng.nextBoolean();

                    coverMaterial = purple
                        ? Block.GRAY_CONCRETE_POWDER
                        : Block.BLACK_CONCRETE_POWDER;
                    underlyingMaterial = purple
                        ? Block.GRAY_CONCRETE
                        : Block.BLACK_CONCRETE;
                }

                unit.modifier().fill(currentPos.withY(MIN_TERRAIN_HEIGHT),
                    tilePos.add(1, 0, 1), underlyingMaterial);

                setBiomeForColumn(unit, currentPos, tileY, biome, floorY);

                // Determine whether to carve by a noise map.
                var carveNoise = OpenSimplex2S.noise2(seed, currentPos.blockX() / MULTIPLY_CARVER_NOISE_BY,
                    currentPos.blockZ() / MULTIPLY_CARVER_NOISE_BY);
                var carve = carveNoise > -0.10 && carveNoise < 0.10;

                if (carve) {
                    carve(unit, humidity, tilePos, tileY);
                    floorMap[x][z] = floorY - 4;
                } else {
                    // Determine where to plant logs (it must have enough space to fit the tree)
                    if (humidity != LocalHumidityType.DRY &&
                        humidity != LocalHumidityType.NORMAL &&
                        logPoint == null &&
                        rng.nextDouble(10) < 9.65D) {

                        var tempLogPoint = floorPos.add(rng.nextInt(10), 0, rng.nextInt(10));

                        var logMin = tempLogPoint.add(-2, 0, -2);
                        var logMax = tempLogPoint.add(2, 0, 2);
                        if (logMin.blockX() > start.blockX() &&
                            logMin.blockZ() > start.blockZ() &&
                            logMax.blockX() < end.blockX() &&
                            logMax.blockZ() < end.blockZ()) {
                            logPoint = tempLogPoint;
                        }
                    }

                    unit.modifier().setBlock(tilePos, coverMaterial);

                    if (humidity != LocalHumidityType.DRY && shouldPutGrass(floorPos, carveNoise)) {
                        unit.modifier().setBlock(floorPos, Block.SHORT_GRASS);
                    }

                    floorMap[x][z] = floorY;
                }

                carveMap[x][z] = carve;
            }
        }

        // Generate log branch
        if (rng.nextBoolean() && logPoint != null &&
            !carveMap[logPoint.blockX() - start.blockX()][logPoint.blockZ() - start.blockZ()]) {
            // Lowers logPoint to the floor
            logPoint = logPoint.withY(floorMap[logPoint.blockX() - start.blockX()][logPoint.blockZ() - start.blockZ()]);

            var treeEnd = logPoint.add(1, TREE_HEIGHT + 1, 1);

            var treeUnit = unit.fork(logPoint.add(2, 0, 2), logPoint.add(-2, 8, -2));
            var mod = treeUnit.modifier();

            mod.fill(logPoint, treeEnd, Block.OAK_LOG);
            mod.setBlock(logPoint.add(0, TREE_HEIGHT + 1, 0), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(1, TREE_HEIGHT, 0), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(0, TREE_HEIGHT, 1), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(1, TREE_HEIGHT, 1), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(-1, TREE_HEIGHT, 0), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(0, TREE_HEIGHT, -1), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(-1, TREE_HEIGHT, -1), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(1, TREE_HEIGHT, -1), Block.OAK_LEAVES);
            mod.setBlock(logPoint.add(-1, TREE_HEIGHT, 1), Block.OAK_LEAVES);
        }
    }

    private double getNoise(Point currentPos, double factor, double offset, double multiplyBy) {
        return (OpenSimplex2.noise2(seed,
            currentPos.blockX() / multiplyBy,
            currentPos.blockZ() / multiplyBy) + offset) *
            factor;
    }

    private static void setBiomeForColumn(GenerationUnit unit, Point currentPos, int tileY, RegistryKey<Biome> biome, int floorY) {
        unit.modifier().setBiome(currentPos.blockX(), tileY - 3, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), tileY - 2, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), tileY - 1, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), tileY, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), floorY, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), floorY + 1, currentPos.blockZ(), biome);
        unit.modifier().setBiome(currentPos.blockX(), floorY + 2, currentPos.blockZ(), biome);
    }

    private static void carve(GenerationUnit unit, LocalHumidityType humidity, Point tilePos, int tileY) {
        var waterBlock = humidity == LocalHumidityType.DRY
            ? Block.AIR
            : Block.WATER;

        // Just carve like this
        unit.modifier().setBlock(tilePos.blockX(), tileY, tilePos.blockZ(), Block.AIR);
        unit.modifier().setBlock(tilePos.blockX(), tileY - 1, tilePos.blockZ(), waterBlock);
        unit.modifier().setBlock(tilePos.blockX(), tileY - 2, tilePos.blockZ(), waterBlock);
        unit.modifier().setBlock(tilePos.blockX(), tileY - 3, tilePos.blockZ(), waterBlock);
        unit.modifier().setBlock(tilePos.blockX(), tileY - 4, tilePos.blockZ(), waterBlock);
    }

}
