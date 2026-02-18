/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapHelperTest {
    @Test
    void testMergeMapsOverwrite() {
        // Arrange
        final var first = Map.of("a", 1,
            "b", 2,
            "c", 3);
        final var second = Map.of("a", 123);

        // Act
        final var result = MapHelper.mergeMaps(first, second);

        // Assert
        assertEquals(3, result.size());
        assertAll(() -> assertEquals(123, result.get("a")),
            () -> assertEquals(2, result.get("b")),
            () -> assertEquals(3, result.get("c")));
    }

    @Test
    void testMergeMapsNotOverwrite() {
        // Arrange
        final var first = Map.of("a", "a",
            "b", "b",
            "c", "c");
        final var second = Map.of("d", "d",
            "e", "e");

        // Act
        final var result = MapHelper.mergeMaps(first, second);

        // Assert
        assertEquals(5, result.size());
        assertAll(() -> assertEquals("a", result.get("a")),
            () -> assertEquals("b", result.get("b")),
            () -> assertEquals("c", result.get("c")),
            () -> assertEquals("d", result.get("d")),
            () -> assertEquals("e", result.get("e")));
    }
}
