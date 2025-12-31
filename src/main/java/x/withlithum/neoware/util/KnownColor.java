package x.withlithum.neoware.util;

import net.minestom.server.codec.Codec;

public enum KnownColor {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    public static final Codec<KnownColor> CODEC = Codec.Enum(KnownColor.class);
}
