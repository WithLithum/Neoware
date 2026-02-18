/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathHelperTest {
    @Test
    void testRemoveExtension() {
        // Arrange
        final var input = "Windows.exe";

        // Act
        final var result = PathHelper.removeExtension(input);

        // Assert
        assertEquals("Windows", result);
    }
}
