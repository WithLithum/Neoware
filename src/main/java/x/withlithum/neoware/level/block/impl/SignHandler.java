package x.withlithum.neoware.level.block.impl;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
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
    public static final Tag<Component> FRONT_TEXT_TAG = Tag.Component("front_text");
    public static final Tag<Component> BACK_TEXT_TAG = Tag.Component("back_text");

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(IS_WAXED_TAG, FRONT_TEXT_TAG, BACK_TEXT_TAG);
    }
}
