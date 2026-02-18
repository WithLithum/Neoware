/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.level;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.registry.RegistryTag;
import net.minestom.server.utils.IntProvider;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.attribute.EnvironmentAttribute;
import net.minestom.server.world.timeline.Timeline;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.level.instances.InstanceCapsule;
import x.withlithum.neoware.level.worldgen.OldWorldColours;
import x.withlithum.neoware.util.KeyRoot;

import java.nio.file.Path;

@NullMarked
public final class LobbyInstance implements InstanceCapsule {
    private static final RegistryKey<DimensionType> DIMENSION_TYPE =
        MinecraftServer.getDimensionTypeRegistry()
            .register(KeyRoot.id("lobby"),
                DimensionType.builder()
                    .minY(-64)
                    .height(320)
                    .logicalHeight(256)
                    .ceiling(false)
                    .fixedTime(true)
                    .infiniburn("#minecraft:infiniburn_overworld")
                    .ambientLight(0F)
                    .monsterSpawnBlockLightLimit(0)
                    .monsterSpawnLightLevel(new IntProvider.Constant(0))
                    .skybox(DimensionType.Skybox.OVERWORLD)
                    .cardinalLight(DimensionType.CardinalLight.DEFAULT)
                    .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.BETA_COLD_SKY_COLOR)
                    .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.BETA_FOG_COLOR)
                    .timelines(RegistryTag.direct(Timeline.DAY, Timeline.MOON))
                    .build());

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyInstance.class);
    private final InstanceContainer instance;

    public LobbyInstance(@Nullable Path path) {
        final var manager = MinecraftServer.getInstanceManager();
        instance = manager.createInstanceContainer(DIMENSION_TYPE);
        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(s -> s.modifier().fillHeight(-63, -62, Block.BARRIER));

        if (path != null) {
            instance.setChunkLoader(new AnvilLoader(path));
        } else {
            LOGGER.warn("Level path is null, only placeholder chunks will be available.");
        }
    }

    @Override
    public Instance instance() {
        return instance;
    }
}
