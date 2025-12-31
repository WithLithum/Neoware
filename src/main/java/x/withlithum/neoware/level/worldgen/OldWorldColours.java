package x.withlithum.neoware.level.worldgen;

import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;

public final class OldWorldColours {
    private OldWorldColours() {
        throw new AssertionError();
    }

    public static final Color OLD_WATER_COLOUR = colour(4610815);

    public static final Color PE_GRASS_COLOUR = colour(3709744);
    public static final Color PE_FOLIAGE_COLOUR = colour(3207444);

    public static final Color PE_SKY_COLOR = colour(2380991);
    public static final Color PE_FOG_COLOR = colour(8444671);

    public static final Color BETA_WARM_SKY_COLOR = colour(6733055);
    public static final Color BETA_TEMP_SKY_COLOR = colour(7777023);
    public static final Color BETA_COOL_SKY_COLOR = colour(8430079);
    public static final Color BETA_COLD_SKY_COLOR = colour(10263039);

    public static final Color BETA_FOG_COLOR = colour(12638463);

    private static Color colour(int value) {
        return new Color(value);
    }
}
