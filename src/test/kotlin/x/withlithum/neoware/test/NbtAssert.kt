/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.test

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import kotlin.test.assertEquals
import kotlin.test.assertIs

fun assertDouble(expected: Double, tag: BinaryTag) {
    val doubleTag = assertIs<DoubleBinaryTag>(tag, "Expected double tag but got ${tag.type()}")
    assertEquals(expected, doubleTag.value())
}