package x.withlithum.neoware.util;

import org.jetbrains.annotations.Contract;

public final class MathHelper {
    private MathHelper() {
        throw new AssertionError("No MathHelper instances for you!");
    }

    @Contract(pure = true)
    public static double lerp(double start, double end, double weight) {
        return (1 - weight) * start + weight * end;
    }
}
