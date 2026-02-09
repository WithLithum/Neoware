/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.minestom.server.codec.Transcoder
import org.junit.jupiter.api.assertAll
import x.withlithum.neoware.data.storage.Vector2F
import x.withlithum.neoware.test.assertFloat
import x.withlithum.neoware.test.assertIsOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class CodecTest {
    @Test
    fun testEncodeVector2() {
        // Arrange
        val value = Vector2F(12.34F, 56.78F)

        // Act
        val result = Vector2FCodec.encode(Transcoder.NBT, value)

        // Assert
        val nbt = assertIs<ListBinaryTag>(assertIsOk(result))
        assertAll({ assertFloat(12.34F, nbt[0]) },
            { assertFloat(56.78F, nbt[1]) },)
    }

    @Test
    fun testDecodeVector2() {
        // Arrange
        val value = ListBinaryTag.from(listOf(DoubleBinaryTag.doubleBinaryTag(1.2),
            DoubleBinaryTag.doubleBinaryTag(3.4)))

        // Act
        val result = Vector2FCodec.decode(Transcoder.NBT, value)

        // Assert
        val vec = assertIsOk(result)
        assertAll({ assertEquals(1.2F, vec.x) },
            { assertEquals(3.4F, vec.y) },)
    }
}