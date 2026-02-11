/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io

import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.minestom.server.codec.Codec
import net.minestom.server.coordinate.Point

object Nbt {
    fun boolean(value: Boolean): ByteBinaryTag {
        return if (value) {
            ByteBinaryTag.ONE
        } else {
            ByteBinaryTag.ZERO
        }
    }
}