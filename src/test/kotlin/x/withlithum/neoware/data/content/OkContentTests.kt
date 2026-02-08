/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import okio.Path.Companion.toPath
import org.junit.jupiter.api.DisplayName
import x.withlithum.neoware.data.content.io.ContentIo
import kotlin.test.Test
import kotlin.test.assertEquals

internal class OkContentTests {
    @Test
    @DisplayName("createKeyPath(): Creates correct path")
    fun testCreatePath() {
        // Arrange
        val root = System.getProperty("user.home").toPath()
        val relative = root.resolve("sub").resolve("test.txt")

        // Act
        val result = ContentIo.createKeyPath(relative, root)

        // Assert
        assertEquals("sub/test", result)
    }
}