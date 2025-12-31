package x.withlithum.neoware.level.block;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import x.withlithum.neoware.util.KnownColor;

import java.util.List;

public record SignText(boolean hasGlowingText,
                       KnownColor color,
                       List<Component> messages) {
    public static final Codec<SignText> CODEC = StructCodec.struct(
        "has_glowing_text", Codec.BOOLEAN, SignText::hasGlowingText,
        "color", KnownColor.CODEC, SignText::color,
        "messages", Codec.COMPONENT.list(4), SignText::messages,
        SignText::new);

    public static final SignText EMPTY = new SignText(false,
        KnownColor.BLACK,
        List.of(Component.empty(), Component.empty(), Component.empty(), Component.empty()));
}
