package x.withlithum.neoware.level.block.impl;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;

@NullMarked
public final class SignHandler implements BlockHandler {
    @Override
    public Key getKey() {
        return Key.key(Key.MINECRAFT_NAMESPACE, "sign");
    }

    public static final Tag<Boolean> IS_WAXED_TAG = Tag.Boolean("is_waxed");
    public static final Tag<BinaryTag> FRONT_TEXT_TAG = Tag.NBT("front_text");
    public static final Tag<BinaryTag> BACK_TEXT_TAG = Tag.NBT("back_text");

    @Override
    public void onTouch(Touch touch) {
        touch.getBlock().getTag(FRONT_TEXT_TAG);
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(IS_WAXED_TAG, FRONT_TEXT_TAG, BACK_TEXT_TAG);
    }
}
