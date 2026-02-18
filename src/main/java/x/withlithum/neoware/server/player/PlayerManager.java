package x.withlithum.neoware.server.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

public interface PlayerManager {
    void filterLogin(PlayerConnection connection,
                     GameProfile profile);

    void configure(AsyncPlayerConfigurationEvent config);

    EventNode<Event> createEventNode();
}
