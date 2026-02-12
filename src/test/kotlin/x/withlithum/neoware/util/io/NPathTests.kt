/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io

import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NPathTests {
    @Test
    fun testRemoveExtension() {
        // Arrange
        val path = "C:\\Windows\\notepad.exe"

        // Act
        val result = NPath.removeExtension(path)

        // Assert
        assertEquals("C:\\Windows\\notepad", result)
    }

    @Test
    fun testGetExtension() {
        // Arrange
        val path = "C:\\Windows\\notepad.exe".toPath()

        // Act
        val result = NPath.getExtension(path)

        // Assert
        assertEquals("exe", result)
    }
}