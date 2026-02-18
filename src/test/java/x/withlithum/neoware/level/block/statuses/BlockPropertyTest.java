/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.statuses;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockPropertyTest {
    @Test
    void testGetHalfUpper() {
        // Arrange
        final var block = Block.DARK_OAK_DOOR
            .withProperty("half", "upper");

        // Act
        final var result = BlockProperties.getHalf(block);

        // Assert
        assertEquals(DoubleBlockHalf.UPPER, result);
    }

    @Test
    void testGetHalfLower() {
        // Arrange
        final var block = Block.DARK_OAK_DOOR
            .withProperty("half", "lower");

        // Act
        final var result = BlockProperties.getHalf(block);

        // Assert
        assertEquals(DoubleBlockHalf.LOWER, result);
    }
}
