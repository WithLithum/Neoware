/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages.extensions

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.ServerSender
import net.minestom.server.component.DataComponents
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import x.withlithum.neoware.util.messages.NeoMessages

fun nameOf(sender: CommandSender): String {
    if (sender is Player) {
        return sender.username
    } else if (sender is Entity) {
        return sender.get(DataComponents.CUSTOM_NAME)
            ?.let { PlainTextComponentSerializer.plainText().serialize(it) }
            ?: sender.entityType.name()
    }

    when (sender) {
        ConsoleSender::class -> return "<Server console>"
        ServerSender::class -> return "<Server>"
    }

    return "<${sender::class.simpleName ?: "unknown"}>"
}

fun Audience.sendNeoSuccess(component: ComponentLike) {
    this.sendMessage(NeoMessages.success(component))
}

fun Audience.sendNeoError(component: ComponentLike) {
    this.sendMessage(NeoMessages.error(component))
}

fun Audience.sendNeoMessage(component: ComponentLike) {
    this.sendMessage(NeoMessages.message(component))
}