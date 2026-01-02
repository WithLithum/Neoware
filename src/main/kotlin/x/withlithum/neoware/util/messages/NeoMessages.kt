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
    val ERROR_PREFIX = createPrefix(NamedTextColor.RED)
    val MESSAGE_PREFIX = createPrefix(NamedTextColor.GOLD)

    private fun createPrefix(color: TextColor): Component {
        return Component.text()
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

    fun message(message: ComponentLike): Component {
        return MESSAGE_PREFIX.append(message)
    }
}