package x.withlithum.neoware.level.worldgen;

import net.minestom.server.color.Color;

public final class OldWorldColours {
    private OldWorldColours() {
        throw new AssertionError();
    }

    public static final Color BETA_COLD_SKY_COLOR = colour(10263039);

    public static final Color BETA_FOG_COLOR = colour(12638463);

    private static Color colour(int value) {
        return new Color(value);
    }
}
