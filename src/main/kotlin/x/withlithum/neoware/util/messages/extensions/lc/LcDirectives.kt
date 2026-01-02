/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages.extensions.lc

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent

fun lc(message: String): TranslatableComponent {
    return Component.translatable("neoware.$message")
}

fun lc(message: String, vararg arguments: Component): Component {
    return Component.translatable()
        .key("neoware.$message")
        .arguments(*arguments)
        .build()
}

fun lcArg(argName: String, message: String): TranslatableComponent {
    return Component.translatable("neoware.arguments.$argName.$message")
}

fun lcArg(argName: String, message: String, vararg arguments: Component): TranslatableComponent {
    return Component.translatable()
        .key("neoware.arguments.$argName.$message")
        .arguments(*arguments)
        .build()
}