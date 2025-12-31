package x.withlithum.neoware.data.player;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import x.withlithum.neoware.data.SavedPosition;
import x.withlithum.neoware.data.game.ItemRef;
import x.withlithum.neoware.level.dimension.KnownInstance;
import x.withlithum.neoware.server.NeoWareServer;
import x.withlithum.neoware.server.commands.PermissionRank;

import java.util.List;

@Slf4j
public record PlayerInfo(int version,
                         PermissionRank rank,
                         PlayerStatus status,
                         SavedPosition position,
                         SavedPosition respawnPoint,
                         KnownInstance lastInstance,
                         @Nullable List<ItemRef> items) {
    public static final Codec<PlayerInfo> CODEC = StructCodec.struct(
        "version", Codec.INT, PlayerInfo::version,
        "rank", PermissionRank.CODEC, PlayerInfo::rank,
        "status", PlayerStatus.CODEC, PlayerInfo::status,
        "position", SavedPosition.CODEC, PlayerInfo::position,
        "respawn_point", SavedPosition.CODEC, PlayerInfo::respawnPoint,
        "last_instance", KnownInstance.CODEC, PlayerInfo::lastInstance,
        "items", ItemRef.CODEC.list().optional(), PlayerInfo::items,
        PlayerInfo::new
    );

    @Deprecated
    public static PlayerInfo create(Player player) {
        return new PlayerInfo(MinecraftServer.DATA_VERSION,
            PermissionRank.values()[player.getPermissionLevel()],
            PlayerStatus.create(player),
            SavedPosition.create(player.getPosition()),
            SavedPosition.create(player.getRespawnPoint()),
            NeoWareServer.INSTANCE.levelOrchestrator.toKnownInstance(player.getInstance()),
            null
        );
    }

    @Deprecated
    public void apply(Player player, boolean setInstance) {
        player.setPermissionLevel(rank.ordinal());
        status.apply(player);
        player.teleport(position.asPos());
        player.setRespawnPoint(respawnPoint.asPos());

        if (setInstance) {
            player.setInstance(NeoWareServer.INSTANCE.levelOrchestrator.getKnownInstance(lastInstance));
        }
        player.getInventory().clear();

        if (player.getInventory().getSize() != items.size()) {
            log.warn("Inventory size mismatch for player '{}' ({}): stored {} versus {}",
                player.getUsername(),
                player.getUuid(),
                items.size(),
                player.getInventory().getSize());
        } else {
            var itemArray = items.toArray(new ItemStack[0]);
            player.getInventory().copyContents(itemArray);
        }
    }
}
