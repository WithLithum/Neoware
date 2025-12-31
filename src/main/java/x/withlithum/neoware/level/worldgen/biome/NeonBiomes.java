package x.withlithum.neoware.level.worldgen.biome;

import net.kyori.adventure.key.KeyPattern;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.attribute.EnvironmentAttribute;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.level.worldgen.OldWorldColours;
import x.withlithum.neoware.util.KeyRoot;

public final class NeonBiomes {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeonBiomes.class);

    private NeonBiomes() {
        throw new AssertionError("No NeonBiomes instances for you!");
    }

    public static final BiomeEffects PE_EFFECT = BiomeEffects.builder()
        .grassColor(OldWorldColours.PE_GRASS_COLOUR)
        .foliageColor(OldWorldColours.PE_FOLIAGE_COLOUR)
        .dryFoliageColor(OldWorldColours.PE_FOLIAGE_COLOUR)
        .waterColor(OldWorldColours.OLD_WATER_COLOUR)
        .build();

    public static final RegistryKey<Biome> PHASE_WETLANDS = register("phase_wetlands",
        Biome.builder()
            .temperature(0.65f)
            .downfall(0.85f)
            .precipitation(true)
            .effects(PE_EFFECT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.PE_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.PE_FOG_COLOR)
            .build());

    public static final RegistryKey<Biome> SEASONAL_PHASE_LAND = register("seasonal_phase_land",
        Biome.builder()
            .temperature(0.55f)
            .downfall(0.60f)
            .precipitation(true)
            .effects(PE_EFFECT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.PE_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.PE_FOG_COLOR)
            .build());

    public static final RegistryKey<Biome> BORDERLINE_DRY_LAND = register("borderline_dry_land",
        Biome.builder()
            .temperature(0.35f)
            .downfall(0.35f)
            .precipitation(true)
            .effects(BiomeEffects.DEFAULT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.PE_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.PE_FOG_COLOR)
            .build());

    public static final RegistryKey<Biome> PHASE_DESERT = register("phase_desert",
        Biome.builder()
            .temperature(0.25f)
            .downfall(0.01f)
            .precipitation(false)
            .effects(PE_EFFECT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.PE_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.PE_FOG_COLOR)
            .build());

    public static final RegistryKey<Biome> CORRUPTED = register("corrupted",
        Biome.builder()
            .temperature(0.25f)
            .downfall(0.01f)
            .precipitation(false)
            .effects(BiomeEffects.builder()
                .grassColor(new Color(3997869)) // #3d00ad
                .foliageColor(new Color(4923294)) // #4b1f9e
                .dryFoliageColor(new Color(4923294)) // #4b1f9e
                .waterColor(new Color(4470157)) // #44358d
                .build())
            .setAttribute(EnvironmentAttribute.SKY_COLOR, new Color(12624885)) // #c0a3f5
            .setAttribute(EnvironmentAttribute.FOG_COLOR, new Color(14471664)) // #dcd1f0
            .build());

    private static RegistryKey<Biome> register(@KeyPattern.Value String id,
                                                      Biome value) {
        LOGGER.debug("Registering biome {}", id);
        return MinecraftServer.getBiomeRegistry().register(KeyRoot.id(id),
            value);
    }

    public static void initialize() {
        LOGGER.info("Initializing biomes");
    }
}
