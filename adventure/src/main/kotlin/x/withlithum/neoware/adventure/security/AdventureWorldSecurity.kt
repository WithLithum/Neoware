/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.security

import net.minestom.server.entity.GameMode
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.trait.CancellableEvent
import net.minestom.server.event.trait.PlayerInstanceEvent
import x.withlithum.neoware.server.commands.PermissionRank

/**
 * Prevents modification of world unless by an administrator.
 */
object AdventureWorldSecurity {
    private val SYS_OP_RANK = PermissionRank.SYS_OP.ordinal

    fun createNode(): EventNode<Event> {
        val node = EventNode.all("Adventure: World security")
        node.addListener(PlayerBlockBreakEvent::class.java, AdventureWorldSecurity::onPlayerPerformLobbyAction)
        node.addListener(PlayerBlockPlaceEvent::class.java, AdventureWorldSecurity::onPlayerPerformLobbyAction)
        node.addListener(PlayerSpawnEvent::class.java, AdventureWorldSecurity::onPlayerSpawn)
        return node;
    }

    private fun onPlayerSpawn(event: PlayerSpawnEvent) {
        event.player.setGameMode(GameMode.ADVENTURE)
    }

    private fun onPlayerPerformLobbyAction(event: PlayerInstanceEvent) {
        // Prevent block placement unless by sys op

        if (event.player.permissionLevel >=SYS_OP_RANK) {
            return
        }

        if (event is CancellableEvent) {
            event.isCancelled = true
        }
    }
}