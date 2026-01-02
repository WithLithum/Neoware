/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

object NeoMessages {
    val ERROR_PREFIX = createPrefix(NamedTextColor.RED, NamedTextColor.DARK_RED)
    val MESSAGE_PREFIX = createPrefix(NamedTextColor.GOLD)
    val SUCCESS_PREFIX = createPrefix(NamedTextColor.GREEN)

    val ARGUMENT_PLAYER_NOT_FOUND = Component.translatable("neoware.arguments.no_player")

    private fun createPrefix(color: TextColor,
                             overrideMessageColor: TextColor? = null): Component {
        return Component.text()
            .color(overrideMessageColor ?: NamedTextColor.GRAY)
            .append(Component.text()
                .content("[")
                .color(NamedTextColor.DARK_GRAY))
            .append(Component.text()
                .content("!")
                .color(color))
            .append(Component.text()
                .content("] ")
                .color(NamedTextColor.DARK_GRAY))
            .build()
    }

    fun error(message: ComponentLike): Component {
        return ERROR_PREFIX.append(message)
    }

    fun errorArgument(name: String, message: ComponentLike): Component {
        return error(Component.translatable()
            .key("neoware.arguments.error")
            .arguments(Component.text(name),
                message))
    }

    fun message(message: ComponentLike): Component {
        return MESSAGE_PREFIX.append(message)
    }

    fun success(message: ComponentLike): Component {
        return SUCCESS_PREFIX.append(message)
    }
}