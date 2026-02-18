package x.withlithum.neoware.server.commands;

import lombok.Getter;
import net.minestom.server.codec.Codec;
import org.jspecify.annotations.NullMarked;

/**
 * The Neoware definitions for permission levels.
 */
@NullMarked
public enum PermissionRank {
    /**
     * Includes the most basic permissions for gameplay. This is the default rank for new players.
     */
    GUEST(0),
    /**
     * Includes basic permissions to interact with the server.
     */
    MEMBER(1),
    /**
     * Includes additional permissions to moderate the server.
     */
    MODERATOR(2),
    /**
     * Includes additional permissions to perform advanced administrative actions.
     */
    SYS_OP(3),
    /**
     * Includes additional permissions to execute server management commands.
     */
    WHEEL(4);

    public static final Codec<PermissionRank> CODEC = Codec.INT.transform(
        PermissionRank::fromValue,
        PermissionRank::getValue
    );

    @Getter
    private final int value;
    PermissionRank(int value) {
        this.value = value;
    }

    public static PermissionRank fromValue(int value) {
        if (value >= WHEEL.value) {
            return WHEEL;
        }

        return switch (value) {
            case 1 -> MEMBER;
            case 2 -> MODERATOR;
            case 3 -> SYS_OP;
            default -> GUEST;
        };
    }
}
