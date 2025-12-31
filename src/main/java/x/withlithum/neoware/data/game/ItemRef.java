package x.withlithum.neoware.data.game;

import net.kyori.adventure.key.Key;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record ItemRef(Key base,
                      int count,
                      int damage) {
    public static final Codec<ItemRef> CODEC = StructCodec.struct(
        "base", Codec.KEY, ItemRef::base,
        "count", Codec.INT, ItemRef::count,
        "damage", Codec.INT, ItemRef::damage,
        ItemRef::new
    );
}
