/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.helpers

import net.kyori.adventure.text.ComponentLike
import net.minestom.server.command.CommandSender
import x.withlithum.neoware.util.messages.extensions.sendNeoError
import x.withlithum.neoware.util.messages.extensions.sendNeoSuccess

fun succeed(sender: CommandSender, message: ComponentLike): Boolean {
    sender.sendNeoSuccess(message)
    return true
}

fun fail(sender: CommandSender, message: ComponentLike): Boolean {
    sender.sendNeoError(message)
    return false
}