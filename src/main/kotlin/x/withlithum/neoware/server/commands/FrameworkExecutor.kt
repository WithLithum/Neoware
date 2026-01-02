/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.CommandContext

fun interface FrameworkExecutor {
    fun apply(sender: CommandSender, context: CommandContext): Boolean
}

fun interface FrameworkExecutor0 {
    fun apply(sender: CommandSender): Boolean
}

fun interface FrameworkExecutor1<in T1> {
    fun apply(sender: CommandSender, p1: T1): Boolean
}

fun interface FrameworkExecutor2<in T1, in T2> {
    fun apply(sender: CommandSender, p1: T1, p2: T2): Boolean
}

fun interface FrameworkExecutor3<in T1, in T2, in T3> {
    fun apply(sender: CommandSender, p1: T1, p2: T2, p3: T3): Boolean
}

fun interface FrameworkExecutor4<in T1, in T2, in T3, in T4> {
    fun apply(
        sender: CommandSender,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4
    ): Boolean
}

fun interface FrameworkExecutor5<in T1, in T2, in T3, in T4, in T5> {
    fun apply(
        sender: CommandSender,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5
    ): Boolean
}

fun interface FrameworkExecutor6<in T1, in T2, in T3, in T4, in T5, in T6> {
    fun apply(
        sender: CommandSender,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5,
        p6: T6
    ): Boolean
}


