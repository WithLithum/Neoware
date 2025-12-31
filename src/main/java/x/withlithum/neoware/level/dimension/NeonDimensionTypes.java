package x.withlithum.neoware.level.dimension;

import net.kyori.adventure.key.KeyPattern;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.registry.RegistryTag;
import net.minestom.server.utils.IntProvider;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.attribute.BedRule;
import net.minestom.server.world.attribute.EnvironmentAttribute;
import net.minestom.server.world.timeline.Timeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.level.worldgen.OldWorldColours;
import x.withlithum.neoware.util.KeyRoot;

public final class NeonDimensionTypes {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeonDimensionTypes.class);

    public static final RegistryKey<DimensionType> LOBBY = register("lobby",
        DimensionType.builder()
            .minY(-64)
            .height(320)
            .logicalHeight(256)
            .ceiling(false)
            .fixedTime(true)
            .infiniburn("#minecraft:infiniburn_overworld")
            .ambientLight(0)
            .monsterSpawnBlockLightLimit(0)
            .monsterSpawnLightLevel(new IntProvider.Constant(0))
            .skybox(DimensionType.Skybox.OVERWORLD)
            .cardinalLight(DimensionType.CardinalLight.DEFAULT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.BETA_COLD_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.BETA_FOG_COLOR)
            .timelines(RegistryTag.direct(Timeline.DAY, Timeline.MOON))
            .build());

    public static final RegistryKey<DimensionType> VENTURED_PHASES = register("ventured_phases",
        DimensionType.builder()
            .minY(0)
            .height(256)
            .logicalHeight(256)
            .ceiling(false)
            .fixedTime(true)
            .infiniburn("#minecraft:infiniburn_overworld")
            .ambientLight(0.35F)
            .monsterSpawnBlockLightLimit(0)
            .monsterSpawnLightLevel(new IntProvider.Uniform(0, 7))
            .skybox(DimensionType.Skybox.NONE)
            .cardinalLight(DimensionType.CardinalLight.DEFAULT)
            .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.PE_SKY_COLOR)
            .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.PE_FOG_COLOR)
            .setAttribute(EnvironmentAttribute.SKY_LIGHT_COLOR, new Color(0xe5e5e5))
            .setAttribute(EnvironmentAttribute.SKY_LIGHT_FACTOR, 0F)
            .setAttribute(EnvironmentAttribute.BED_RULE, BedRule.EXPLODES)
            .setAttribute(EnvironmentAttribute.RESPAWN_ANCHOR_WORKS, false)
            .timelines(RegistryTag.direct(Timeline.VILLAGER_SCHEDULE))
            .build());

    public static RegistryKey<DimensionType> register(@KeyPattern.Value String id,
                                                      DimensionType value) {
        LOGGER.debug("Registering dimension type {}", id);
        return MinecraftServer.getDimensionTypeRegistry().register(KeyRoot.id(id),
            value);
    }

    public static void initialize() {
        LOGGER.info("Initializing dimension types");
    }
}
