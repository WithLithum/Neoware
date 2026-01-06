package x.withlithum.neoware.data.player;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import org.jetbrains.annotations.Nullable;
import x.withlithum.neoware.data.SavedPosition;
import x.withlithum.neoware.data.game.ItemRef;
import x.withlithum.neoware.server.commands.PermissionRank;

import java.util.List;

@Slf4j
public record PlayerInfo(int version,
                         PermissionRank rank,
                         PlayerStatus status,
                         SavedPosition position,
                         SavedPosition respawnPoint,
                         @Nullable List<ItemRef> items) {
    public static final Codec<PlayerInfo> CODEC = StructCodec.struct(
        "version", Codec.INT, PlayerInfo::version,
        "rank", PermissionRank.CODEC, PlayerInfo::rank,
        "status", PlayerStatus.CODEC, PlayerInfo::status,
        "position", SavedPosition.CODEC, PlayerInfo::position,
        "respawn_point", SavedPosition.CODEC, PlayerInfo::respawnPoint,
        "items", ItemRef.CODEC.list().optional(), PlayerInfo::items,
        PlayerInfo::new
    );
}
