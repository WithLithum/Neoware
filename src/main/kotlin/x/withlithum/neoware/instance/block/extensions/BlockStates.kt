/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.block.extensions

import net.minestom.server.instance.block.Block

fun Block.getBoolean(key: String): Boolean {
    return when(val prop = this.getProperty(key)) {
        "true" -> true
        "false" -> false
        else -> throw IllegalArgumentException("$key is neither 'true' or 'false' (value: '$prop')")
    }
}

fun Block.withProperty(key: String, value: Boolean): Block {
    return this.withProperty(key, when(value) {
        true -> "true"
        false -> "false"
    })
}