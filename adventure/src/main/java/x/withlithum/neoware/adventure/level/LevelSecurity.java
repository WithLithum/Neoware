/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.level;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import x.withlithum.neoware.server.commands.PermissionRank;

public final class LevelSecurity {
    private static final int SYS_OP_RANK = PermissionRank.SYS_OP.ordinal();

    private LevelSecurity() {
        throw new AssertionError("No LevelSecurity instances for you!");
    }

    public static EventNode<Event> createEventNode() {
        final var node = EventNode.all("Adventure server - level security");
        node.addListener(PlayerSpawnEvent.class, LevelSecurity::onPlayerSpawn);
        node.addListener(PlayerBlockPlaceEvent.class, LevelSecurity::onPlayerPerformLobbyAction);
        node.addListener(PlayerBlockBreakEvent.class, LevelSecurity::onPlayerPerformLobbyAction);

        return node;
    }

    private static void onPlayerSpawn(PlayerSpawnEvent event) {
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    private static void onPlayerPerformLobbyAction(PlayerInstanceEvent event) {
        // Prevent block placement unless by sys op

        if (event.getPlayer().getPermissionLevel() >=SYS_OP_RANK) {
            return;
        }

        if (event instanceof CancellableEvent cancel) {
            cancel.setCancelled(true);
        }
    }
}
