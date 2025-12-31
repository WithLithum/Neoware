package x.withlithum.neoware.data.player;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.entity.Player;

public record PlayerStatus(float health,
                           int food,
                           float saturation) {
    public static final Codec<PlayerStatus> CODEC = StructCodec.struct(
        "health", Codec.FLOAT, PlayerStatus::health,
        "food", Codec.INT, PlayerStatus::food,
        "saturation", Codec.FLOAT, PlayerStatus::saturation,
        PlayerStatus::new
    );

    public static PlayerStatus create(Player player) {
        return new PlayerStatus(player.getHealth(),
            player.getFood(),
            player.getFoodSaturation());
    }

    public void apply(Player player) {
        player.setHealth(health);
        player.setFood(food);
        player.setFoodSaturation(saturation);
    }
}
