package x.withlithum.neoware.server.commands;

import net.minestom.server.codec.Codec;

public enum PermissionRank {
    GUEST,
    MEMBER,
    MODERATOR,
    SYS_OP,
    /**
     * Includes additional permissions to execute server management commands.
     */
    WHEEL;

    public static final Codec<PermissionRank> CODEC = Codec.Enum(PermissionRank.class);
}
