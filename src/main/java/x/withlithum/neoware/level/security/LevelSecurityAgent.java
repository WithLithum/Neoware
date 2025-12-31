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
import x.withlithum.neoware.level.dimension.KnownInstance;
import x.withlithum.neoware.server.NeoWareServer;
import x.withlithum.neoware.server.commands.PermissionRank;

public final class LevelSecurityAgent {
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
        var levelOrc = NeoWareServer.INSTANCE.levelOrchestrator;
        var targetMode = switch (levelOrc.toKnownInstance(event.getInstance())) {
            case KnownInstance.LOBBY -> GameMode.ADVENTURE;
            case KnownInstance.VENTURED_PHASES -> GameMode.SURVIVAL;
        };
        event.getPlayer().setGameMode(targetMode);
    }

    private static void onPlayerPerformLobbyAction(PlayerInstanceEvent event) {
        // Prevent block placement on lobby unless by sys op
        if (event.getInstance() != NeoWareServer.INSTANCE.levelOrchestrator.lobbyLevel) {
            return;
        }

        if (event.getPlayer().getPermissionLevel() >= PermissionRank.SYS_OP.ordinal()) {
            return;
        }

        if (event instanceof CancellableEvent cancellableEvent) {
            cancellableEvent.setCancelled(true);
        }
    }
}
