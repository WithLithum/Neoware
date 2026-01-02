/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer
import x.withlithum.neoware.util.messages.NeoMessages

object CommandFramework {
    private val unknownCommand = Component.translatable()
        .key("neoware.commands.unknown")
        .color(NamedTextColor.RED)
        .build()

    fun initialize() {
        MinecraftServer.getCommandManager().unknownCommandCallback = { sender, command ->
            sender.sendMessage(
                NeoMessages.error(
                    unknownCommand.arguments(
                        Component.text()
                            .content(command)
                            .color(NamedTextColor.YELLOW)
                            .decoration(TextDecoration.BOLD, false)
                    )
                )
            )
        }
    }

    fun register(command: FxCommand) {
        MinecraftServer.getCommandManager().register(command.build())
    }
}