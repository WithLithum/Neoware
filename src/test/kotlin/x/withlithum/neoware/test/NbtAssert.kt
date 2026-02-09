/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.test

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import kotlin.test.assertEquals
import kotlin.test.assertIs

fun assertFloat(expected: Float, tag: BinaryTag) {
    val floatTag = assertIs<FloatBinaryTag>(tag, "Expected float tag but got ${tag.type()}")
    assertEquals(expected, floatTag.value())
}

fun assertDouble(expected: Double, tag: BinaryTag) {
    val doubleTag = assertIs<DoubleBinaryTag>(tag, "Expected double tag but got ${tag.type()}")
    assertEquals(expected, doubleTag.value())
}