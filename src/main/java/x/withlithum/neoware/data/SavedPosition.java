package x.withlithum.neoware.data;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.coordinate.Pos;

public record SavedPosition(double x,
                            double y,
                            double z,
                            float yaw,
                            float pitch) {
    public static final Codec<SavedPosition> CODEC = StructCodec.struct(
        "x", Codec.DOUBLE, SavedPosition::x,
        "y", Codec.DOUBLE, SavedPosition::y,
        "z", Codec.DOUBLE, SavedPosition::z,
        "yaw", Codec.FLOAT, SavedPosition::yaw,
        "pitch", Codec.FLOAT, SavedPosition::pitch,
        SavedPosition::new
    );

    public static SavedPosition create(Pos pos) {
        return new SavedPosition(pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch());
    }

    public Pos asPos() {
        return new Pos(x, y, z, yaw, pitch);
    }
}
