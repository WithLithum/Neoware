/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.server

import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatureSet
import io.github.togar2.pvp.feature.CombatFeatures
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.adventure.security.AdventureWorldSecurity
import x.withlithum.neoware.data.content.hierarchy.ContentTree
import x.withlithum.neoware.data.content.packs.ContentPackLoader
import x.withlithum.neoware.framework.server.NeoFrameworkServer
import x.withlithum.neoware.adventure.level.LobbyInstance
import x.withlithum.neoware.instance.ManagedInstance
import x.withlithum.neoware.server.config.Configs

class NeoAdventureServer(basePath: Path) : NeoFrameworkServer(Configs.getEndpoint(),
    basePath) {
    private var contents: ContentTree? = null
    override fun createInstance(): ManagedInstance {
        return LobbyInstance()
    }

    override fun bootstrap() {
        contents = ContentPackLoader.loadAll(basePath.resolve("content"),
            FileSystem.SYSTEM)

        // PVP
        MinestomPvP.init()
        val modernVanilla: CombatFeatureSet = CombatFeatures.modernVanilla()

        // Events registration
        val node = EventNode.all("Neo Adventure Server")
        node.addChild(modernVanilla.createNode())

        MinecraftServer.getGlobalEventHandler().addChild(node)
        MinecraftServer.getGlobalEventHandler().addChild(AdventureWorldSecurity.createNode())
    }
}