package x.withlithum.neoware.data.player;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.entity.Player;
import x.withlithum.neoware.data.SavedPosition;

public record PlayerStatus(float health,
                           int food,
                           float saturation,
                           SavedPosition respawnPoint,
                           SavedPosition position) {
    public static final Codec<PlayerStatus> CODEC = StructCodec.struct(
        "health", Codec.FLOAT, PlayerStatus::health,
        "food", Codec.INT, PlayerStatus::food,
        "saturation", Codec.FLOAT, PlayerStatus::saturation,
        "respawn_point", SavedPosition.CODEC, PlayerStatus::respawnPoint,
        "position", SavedPosition.CODEC, PlayerStatus::position,
        PlayerStatus::new
    );

    public static PlayerStatus create(Player player) {
        return new PlayerStatus(player.getHealth(),
            player.getFood(),
            player.getFoodSaturation(),
            SavedPosition.create(player.getRespawnPoint()),
            SavedPosition.create(player.getPosition()));
    }

    public void apply(Player player) {
        player.setHealth(health);
        player.setFood(food);
        player.setFoodSaturation(saturation);
        player.setRespawnPoint(respawnPoint.asPos());
        player.teleport(position.asPos());
    }
}
