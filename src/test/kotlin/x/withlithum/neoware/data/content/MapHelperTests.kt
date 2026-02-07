/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import x.withlithum.neoware.util.MapHelper
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MapHelperTests {
    @Test
    @DisplayName("mergeMaps(): Overwrites correctly")
    fun testMergeMapsOverwrite() {
        // Arrange
        val first = mapOf("a" to 1, "b" to 2, "c" to 3)
        val second = mapOf("a" to 123)

        // Act
        val result = MapHelper.mergeMaps(first, second)

        // Assert
        assertEquals(3, result.size)
        assertAll({ assertEquals(123, result["a"]) },
            { assertEquals(2, result["b"]) },
            { assertEquals(3, result["c"]) })
    }

    @Test
    @DisplayName("mergeMaps(): Merges without overwrite correctly")
    fun testMergeMapsNotOverwrite() {
        // Arrange
        val first = mapOf("a" to "a", "b" to "b", "c" to "c")
        val second = mapOf("d" to "d", "e" to "e")

        // Act
        val result = MapHelper.mergeMaps(first, second)

        // Assert
        assertEquals(5, result.size)
        assertAll({ assertEquals("a", result["a"]) },
            { assertEquals("b", result["b"]) },
            { assertEquals("c", result["c"]) },
            { assertEquals("d", result["d"]) },
            { assertEquals("e", result["e"]) })
    }
}