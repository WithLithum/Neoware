/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.block.statuses

import net.minestom.server.instance.block.Block
import x.withlithum.neoware.instance.block.extensions.*

object BlockStates {
    const val OPEN_PROPERTY = "open"
    const val HALF_PROPERTY = "half"

    fun isOpen(block: Block): Boolean {
        return block.getBoolean(OPEN_PROPERTY)
    }

    fun withOpen(block: Block, open: Boolean): Block {
        return block.withProperty(OPEN_PROPERTY, open)
    }

    fun getDoubleHalf(block: Block): DoubleBlockHalf {
        return when(val prop = block.getProperty(HALF_PROPERTY)) {
            "upper" -> DoubleBlockHalf.UPPER
            "lower" -> DoubleBlockHalf.LOWER
            else -> throw IllegalArgumentException("Invalid half (value: $prop)")
        }
    }
}