/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.util.messages.extensions.sendNeoMessage

class FxAboutCommand : FxCommand("about") {
    private val copyrightMessage = lc("copyright")
    private val minestomMessage = lc(
        "minestom", Component.text()
            .content(MinecraftServer.VERSION_NAME)
            .color(NamedTextColor.YELLOW)
            .build()
    ).color(NamedTextColor.GRAY)
    private val sourceMessage = lc("source_link")
        .color(NamedTextColor.BLUE)
        .decorate(TextDecoration.UNDERLINED)
        .clickEvent(ClickEvent.openUrl("https://gitea.com/WithLithum/neoware"))

    override fun construct() {
        syntax { sender ->
            sender.sendNeoMessage(copyrightMessage)
            sender.sendMessage(minestomMessage)
            sender.sendMessage(Component.empty())
            sender.sendMessage(Component.text()
                .content("[")
                .color(NamedTextColor.DARK_GRAY)
                .append(sourceMessage)
                .append(Component.text("]")))

            true
        }
    }
}