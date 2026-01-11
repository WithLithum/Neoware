/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import org.junit.jupiter.api.DisplayName
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FileSystemContentTests {
    @Test
    @DisplayName("createKeyPath(): Creates correct path")
    fun testCreatePath() {
        // Arrange
        val base = Path.of(System.getProperty("user.home"))
        val relative = base.resolve("sub", "test.txt")

        // Act
        val result = FileSystemContentSource.createKeyPath(relative, base)

        // Assert
        assertEquals("sub/test", result)
    }
}