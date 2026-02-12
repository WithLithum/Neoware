package x.withlithum.neoware.data.game;

import net.kyori.adventure.key.Key;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.Objects;

public record ItemRef(Key base,
                      int count,
                      int damage) {
    public static final Key AIR_KEY = Key.key("neoware", "air");
    public static final ItemRef AIR = new ItemRef(AIR_KEY, 0, 0);

    public static final Codec<ItemRef> CODEC = StructCodec.struct(
        "base", Codec.KEY, ItemRef::base,
        "count", Codec.INT, ItemRef::count,
        "damage", Codec.INT, ItemRef::damage,
        ItemRef::new
    );

    public boolean isAir() {
        return equals(AIR);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemRef(Key base1, int count1, int damage1))) return false;
        return count == count1 && damage == damage1 && Objects.equals(base, base1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, count, damage);
    }
}
