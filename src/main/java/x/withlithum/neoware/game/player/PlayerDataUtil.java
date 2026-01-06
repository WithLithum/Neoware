package x.withlithum.neoware.game.player;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.data.SavedPosition;
import x.withlithum.neoware.data.player.PlayerInfo;
import x.withlithum.neoware.data.player.PlayerStatus;
import x.withlithum.neoware.game.inventory.InventoryResolver;
import x.withlithum.neoware.server.commands.PermissionRank;

@Slf4j
@NullMarked
public final class PlayerDataUtil {
    public static final int VERSION = 2;

    public static void recoverPlayer(Player player, PlayerInfo info) {
        player.setPermissionLevel(info.rank().ordinal());
        info.status().apply(player);
        player.teleport(info.position().asPos());
        player.setRespawnPoint(info.respawnPoint().asPos());
        player.getInventory().clear();

        if (info.items() != null) {
            if (player.getInventory().getSize() != info.items().size()) {
                log.warn("Inventory size mismatch for player '{}' ({}): stored {} versus {}",
                    player.getUsername(),
                    player.getUuid(),
                    info.items().size(),
                    player.getInventory().getSize());
                log.warn("Not recovering inventory");
            } else {
                InventoryResolver.recover(info.items(), player.getInventory());
            }
        }
    }

    public static PlayerInfo createPlayerInfo(Player player) {
        var items = InventoryResolver.resolve(player.getInventory());

        return new PlayerInfo(VERSION,
            PermissionRank.values()[player.getPermissionLevel()],
            PlayerStatus.create(player),
            SavedPosition.create(player.getPosition()),
            SavedPosition.create(player.getRespawnPoint()),
            items
        );
    }
}
