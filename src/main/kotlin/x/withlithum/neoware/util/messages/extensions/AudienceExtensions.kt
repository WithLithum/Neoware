/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages.extensions

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.ComponentLike
import x.withlithum.neoware.util.messages.NeoMessages

fun Audience.sendNeoSuccess(component: ComponentLike) {
    this.sendMessage(NeoMessages.success(component))
}

fun Audience.sendNeoError(component: ComponentLike) {
    this.sendMessage(NeoMessages.error(component))
}

fun Audience.sendNeoMessage(component: ComponentLike) {
    this.sendMessage(NeoMessages.message(component))
}