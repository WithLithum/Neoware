/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.Instance
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.registry.RegistryTag
import net.minestom.server.utils.IntProvider
import net.minestom.server.world.DimensionType
import net.minestom.server.world.attribute.EnvironmentAttribute
import net.minestom.server.world.timeline.Timeline
import x.withlithum.neoware.level.worldgen.OldWorldColours
import x.withlithum.neoware.server.config.Configs
import x.withlithum.neoware.util.KeyRoot

class LobbyInstance {
    val instance: Instance

    companion object {
        private val LOG = KotlinLogging.logger { }

        private val DIMENSION_TYPE = MinecraftServer.getDimensionTypeRegistry()
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
                    .monsterSpawnLightLevel(IntProvider.Constant(0))
                    .skybox(DimensionType.Skybox.OVERWORLD)
                    .cardinalLight(DimensionType.CardinalLight.DEFAULT)
                    .setAttribute(EnvironmentAttribute.SKY_COLOR, OldWorldColours.BETA_COLD_SKY_COLOR)
                    .setAttribute(EnvironmentAttribute.FOG_COLOR, OldWorldColours.BETA_FOG_COLOR)
                    .timelines(RegistryTag.direct(Timeline.DAY, Timeline.MOON))
                    .build())
    }

    constructor() {
        val manager = MinecraftServer.getInstanceManager()
        instance = manager.createInstanceContainer(DIMENSION_TYPE)
        instance.chunkSupplier = { i, x, z -> LightingChunk(i, x, z) }
        instance.setGenerator {
            it.modifier().fillHeight(-63, -62, Block.BARRIER)
        }

        // Set level loader
        if (Configs.get().hasPath(Configs.KEY_LOBBY_LEVEL)) {
            instance.chunkLoader = AnvilLoader(Configs.get().getString(Configs.KEY_LOBBY_LEVEL))
        } else {
            LOG.warn { "No lobby world file specified, using placeholder generator for entire map" }
        }
    }
}