/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.extensions

import net.minestom.server.command.builder.arguments.Argument

fun <V> Argument<V>.makeOptional(): Argument<V?> {
    val nulValue: V? = null
    return this.setDefaultValue(nulValue)
}