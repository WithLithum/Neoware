/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages.extensions

import net.kyori.adventure.text.Component

fun lcGlobal(key: String): Component {
    return Component.translatable(key)
}