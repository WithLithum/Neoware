package x.withlithum.neoware.level.security;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import x.withlithum.neoware.server.commands.PermissionRank;

public final class LevelSecurityAgent {
    private static final int SYS_OP_RANK = PermissionRank.SYS_OP.ordinal();

    private LevelSecurityAgent() {}

    public static EventNode<Event> createEventNode() {
        var result = EventNode.type("LevelSecurityAgent", EventFilter.ALL);

        var blockNode = EventNode.type("LSA: Block", EventFilter.BLOCK);
        result.addListener(PlayerBlockBreakEvent.class, LevelSecurityAgent::onPlayerPerformLobbyAction);
        result.addListener(PlayerBlockPlaceEvent.class, LevelSecurityAgent::onPlayerPerformLobbyAction);
        result.addChild(blockNode);

        var instanceNode = EventNode.type("LSA: Instance", EventFilter.INSTANCE);
        instanceNode.addListener(PlayerSpawnEvent.class, LevelSecurityAgent::onPlayerSpawn);

        return result;
    }

    private static void onPlayerSpawn(PlayerSpawnEvent event) {
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    private static void onPlayerPerformLobbyAction(PlayerInstanceEvent event) {
        // Prevent block placement unless by sys op

        if (event.getPlayer().getPermissionLevel() >= SYS_OP_RANK) {
            return;
        }

        if (event instanceof CancellableEvent cancellableEvent) {
            cancellableEvent.setCancelled(true);
        }
    }
}
