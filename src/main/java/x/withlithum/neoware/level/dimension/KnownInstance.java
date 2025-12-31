package x.withlithum.neoware.level.dimension;

import net.minestom.server.codec.Codec;

public enum KnownInstance {
    LOBBY,
    VENTURED_PHASES;

    public static final Codec<KnownInstance> CODEC = Codec.Enum(KnownInstance.class);
}
