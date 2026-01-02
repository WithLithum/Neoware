/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandData
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.condition.CommandCondition
import x.withlithum.neoware.util.messages.NeoMessages
import x.withlithum.neoware.util.messages.extensions.sendNeoError

@Suppress("SameParameterValue")
abstract class FxCommand(val name: String) {
    private val command = Command(name)
    private val lcParentKey = "neoware.commands.$name"

    abstract fun construct()

    protected fun lc(key: String): Component {
        return Component.translatable(key)
    }

    protected fun lc(key: String, p1: ComponentLike): Component {
        return Component.translatable()
            .key(key)
            .arguments(p1)
            .build()
    }

    protected fun syntaxEx(
        executor: FrameworkExecutor,
        vararg arguments: Argument<Any>
    ) {
        conditionalSyntaxEx(null, executor, *arguments)
    }

    //#region Framework syntax overloads

    protected fun syntax(executor: FrameworkExecutor0) {
        conditionalSyntax(null, executor)
    }

    protected fun <T1> syntax(
        p1: Argument<T1>,
        executor: FrameworkExecutor1<T1>
    ) {
        conditionalSyntax(null, p1, executor)
    }

    protected fun <T1, T2> syntax(
        p1: Argument<T1>,
        p2: Argument<T2>,
        executor: FrameworkExecutor2<T1, T2>
    ) {
        conditionalSyntax(null, p1, p2, executor)
    }

    protected fun <T1, T2, T3> syntax(
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        executor: FrameworkExecutor3<T1, T2, T3>
    ) {
        conditionalSyntax(null, p1, p2, p3, executor)
    }

    protected fun <T1, T2, T3, T4> syntax(
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        executor: FrameworkExecutor4<T1, T2, T3, T4>
    ) {
        conditionalSyntax(null, p1, p2, p3, p4, executor)
    }

    protected fun <T1, T2, T3, T4, T5> syntax(
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        p5: Argument<T5>,
        executor: FrameworkExecutor5<T1, T2, T3, T4, T5>
    ) {
        conditionalSyntax(null, p1, p2, p3, p4, p5, executor)
    }

    protected fun <T1, T2, T3, T4, T5, T6> syntax(
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        p5: Argument<T5>,
        p6: Argument<T6>,
        executor: FrameworkExecutor6<T1, T2, T3, T4, T5, T6>
    ) {
        conditionalSyntax(null, p1, p2, p3, p4, p5, p6, executor)
    }

    //#endregion

    protected fun conditionalSyntaxEx(
        condition: CommandCondition?,
        executor: FrameworkExecutor,
        vararg arguments: Argument<*>
    ) {
        command.addConditionalSyntax(condition, { sender, ctx ->
            if (condition?.canUse(sender, ctx.input) == false
                || command.condition?.canUse(sender, ctx.input) == false
            ) {
                ctx.returnData = CommandData()
                    .set("success", false)
                sender.sendMessage(NeoMessages.error(Component.translatable("neoware.commands.no_permission")))

                return@addConditionalSyntax
            }

            val data = CommandData()
                .set("success", executor.apply(sender, ctx))

            ctx.returnData = data
        }, *arguments)
    }

    //#region Framework conditional syntax overloads

    protected fun conditionalSyntax(
        condition: CommandCondition?,
        executor: FrameworkExecutor0
    ) {
        conditionalSyntaxEx(condition, { sender, _ ->
            executor.apply(sender)
        })
    }

    protected fun <T1> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        executor: FrameworkExecutor1<T1>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(sender, ctx.get(p1))
        }, p1)
    }

    protected fun <T1, T2> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        p2: Argument<T2>,
        executor: FrameworkExecutor2<T1, T2>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(
                sender, ctx.get(p1),
                ctx.get(p2)
            )
        }, p1, p2)
    }

    protected fun <T1, T2, T3> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        executor: FrameworkExecutor3<T1, T2, T3>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(
                sender,
                ctx.get(p1),
                ctx.get(p2),
                ctx.get(p3)
            )
        }, p1, p2, p3)
    }

    protected fun <T1, T2, T3, T4> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        executor: FrameworkExecutor4<T1, T2, T3, T4>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(
                sender,
                ctx.get(p1),
                ctx.get(p2),
                ctx.get(p3),
                ctx.get(p4),
            )
        }, p1, p2, p3, p4)
    }

    protected fun <T1, T2, T3, T4, T5> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        p5: Argument<T5>,
        executor: FrameworkExecutor5<T1, T2, T3, T4, T5>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(
                sender,
                ctx.get(p1),
                ctx.get(p2),
                ctx.get(p3),
                ctx.get(p4),
                ctx.get(p5),
            )
        }, p1, p2, p3, p4, p5)
    }

    protected fun <T1, T2, T3, T4, T5, T6> conditionalSyntax(
        condition: CommandCondition?,
        p1: Argument<T1>,
        p2: Argument<T2>,
        p3: Argument<T3>,
        p4: Argument<T4>,
        p5: Argument<T5>,
        p6: Argument<T6>,
        executor: FrameworkExecutor6<T1, T2, T3, T4, T5, T6>
    ) {
        conditionalSyntaxEx(condition, { sender, ctx ->
            executor.apply(
                sender,
                ctx.get(p1),
                ctx.get(p2),
                ctx.get(p3),
                ctx.get(p4),
                ctx.get(p5),
                ctx.get(p6)
            )
        }, p1, p2, p3, p4, p5, p6)
    }

    //#endregion

    fun build(): Command {
        construct()

        return command
    }
}