/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.statuses;

import net.minestom.server.instance.block.Block;

public final class BlockProperties {
    private BlockProperties() {
        throw new AssertionError("No BlockProperties instances for you!");
    }

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public static final String PROPERTY_OPEN = "open";
    public static final String PROPERTY_HALF = "half";

    public static boolean isOpen(Block block) {
        return VALUE_TRUE.equals(block.getProperty(PROPERTY_OPEN));
    }

    public static Block mutateOpen(Block block, boolean open) {
        return block.withProperty(PROPERTY_OPEN, open ? VALUE_TRUE : VALUE_FALSE);
    }

    public static DoubleBlockHalf getHalf(Block block) {
        final var half = block.getProperty(PROPERTY_HALF);
        if (half == null) {
            return DoubleBlockHalf.LOWER;
        }

        return switch (half) {
            case "lower" -> DoubleBlockHalf.UPPER;
            case "upper" -> DoubleBlockHalf.LOWER;
            default -> throw new IllegalArgumentException("Invalid block half value: " + half);
        };
    }
}
