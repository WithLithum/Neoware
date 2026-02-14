/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandData;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.condition.CommandCondition;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.framework.server.NeoFrameworkServer;
import x.withlithum.neoware.server.commands.executors.*;
import x.withlithum.neoware.util.text.Messages;

import java.awt.*;

@NullMarked
@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class CommandSkeleton implements CommandBuildable {
    public static final String COMMAND_DATA_SUCCESS = "success";

    @Getter
    private final String name;
    protected final Command command;

    protected CommandSkeleton(String name) {
        this.name = name;
        this.command = new Command(name);
    }

    public abstract void construct(NeoFrameworkServer server);

    //#region Translation messages
    protected static boolean succeed(CommandSender sender, ComponentLike message) {
        Messages.sendSuccess(sender, message.asComponent());
        return true;
    }

    protected static boolean failure(CommandSender sender, ComponentLike message) {
        Messages.sendError(sender, message.asComponent());
        return true;
    }

    protected TranslatableComponent message(String key) {
        return Component.translatable(String.format("neoware.commands.%1$s.%2$s", name, key));
    }

    protected TranslatableComponent message(String key, ComponentLike... args) {
        return Component.translatable()
            .key(String.format("neoware.commands.%1$s.%2$s", name, key))
            .arguments(args)
            .build();
    }
    //#endregion

    protected final void registerSyntax(@Nullable CommandCondition condition,
                                        CommandFunction executor,
                                        Argument<?>... arguments) {
        command.addConditionalSyntax(condition,
            (sender, context) -> {
                if ((condition != null && !condition.canUse(sender, context.getInput()))
                    || (command.getCondition() != null && !command.getCondition().canUse(sender, context.getInput()))) {
                    context.setReturnData(new CommandData()
                        .set(COMMAND_DATA_SUCCESS, false));
                    Messages.sendError(sender, Component.translatable("neoware.commands.no_permission"));
                    return;
                }

                final var data = new CommandData()
                    .set(COMMAND_DATA_SUCCESS, executor.apply(sender, context));
                context.setReturnData(data);
            }
            , arguments);
    }

    //#region Syntax registration

    protected final void conditionalSyntax(@Nullable CommandCondition condition,
                                           CommandFunction0 executor) {
        registerSyntax(condition,
            (sender, context) -> executor.apply(sender));
    }

    protected final <T1> void conditionalSyntax(@Nullable CommandCondition condition,
                                                Argument<T1> p1,
                                                CommandFunction1<T1> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1)));
    }

    protected final <T1, T2> void conditionalSyntax(@Nullable CommandCondition condition,
                                                    Argument<T1> p1,
                                                    Argument<T2> p2,
                                                    CommandFunction2<T1, T2> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1), context.get(p2)));
    }

    protected final <T1, T2, T3> void conditionalSyntax(@Nullable CommandCondition condition,
                                                        Argument<T1> p1,
                                                        Argument<T2> p2,
                                                        Argument<T3> p3,
                                                        CommandFunction3<T1, T2, T3> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1), context.get(p2), context.get(p3)));
    }

    protected final <T1, T2, T3, T4> void conditionalSyntax(@Nullable CommandCondition condition,
                                                            Argument<T1> p1,
                                                            Argument<T2> p2,
                                                            Argument<T3> p3,
                                                            Argument<T4> p4,
                                                            CommandFunction4<T1, T2, T3, T4> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1),
                context.get(p2),
                context.get(p3),
                context.get(p4))
        );
    }

    protected final <T1, T2, T3, T4, T5> void conditionalSyntax(@Nullable CommandCondition condition,
                                                                Argument<T1> p1,
                                                                Argument<T2> p2,
                                                                Argument<T3> p3,
                                                                Argument<T4> p4,
                                                                Argument<T5> p5,
                                                                CommandFunction5<T1, T2, T3, T4, T5> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1),
                context.get(p2),
                context.get(p3),
                context.get(p4),
                context.get(p5))
        );
    }

    protected final <T1, T2, T3, T4, T5, T6> void conditionalSyntax(@Nullable CommandCondition condition,
                                                                    Argument<T1> p1,
                                                                    Argument<T2> p2,
                                                                    Argument<T3> p3,
                                                                    Argument<T4> p4,
                                                                    Argument<T5> p5,
                                                                    Argument<T6> p6,
                                                                    CommandFunction6<T1, T2, T3, T4, T5, T6> executor) {
        registerSyntax(condition, (sender, context) ->
            executor.apply(sender, context.get(p1),
                context.get(p2),
                context.get(p3),
                context.get(p4),
                context.get(p5),
                context.get(p6))
        );
    }

    protected final void syntax(CommandFunction0 executor) {
        conditionalSyntax(null, executor);
    }

    protected final <T1> void syntax(Argument<T1> p1,
                                     CommandFunction1<T1> executor) {
        conditionalSyntax(null, p1, executor);
    }

    protected final <T1, T2> void syntax(Argument<T1> p1,
                                         Argument<T2> p2,
                                         CommandFunction2<T1, T2> executor) {
        conditionalSyntax(null, p1, p2, executor);
    }

    protected final <T1, T2, T3> void syntax(Argument<T1> p1,
                                             Argument<T2> p2,
                                             Argument<T3> p3,
                                             CommandFunction3<T1, T2, T3> executor) {
        conditionalSyntax(null, p1, p2, p3, executor);
    }

    protected final <T1, T2, T3, T4> void syntax(Argument<T1> p1,
                                                 Argument<T2> p2,
                                                 Argument<T3> p3,
                                                 Argument<T4> p4,
                                                 CommandFunction4<T1, T2, T3, T4> executor) {
        conditionalSyntax(null, p1, p2, p3, p4, executor);
    }

    protected final <T1, T2, T3, T4, T5> void syntax(Argument<T1> p1,
                                                     Argument<T2> p2,
                                                     Argument<T3> p3,
                                                     Argument<T4> p4,
                                                     Argument<T5> p5,
                                                     CommandFunction5<T1, T2, T3, T4, T5> executor) {
        conditionalSyntax(null, p1, p2, p3, p4, p5, executor);
    }

    protected final <T1, T2, T3, T4, T5, T6> void syntax(Argument<T1> p1,
                                                         Argument<T2> p2,
                                                         Argument<T3> p3,
                                                         Argument<T4> p4,
                                                         Argument<T5> p5,
                                                         Argument<T6> p6,
                                                         CommandFunction6<T1, T2, T3, T4, T5, T6> executor) {
        conditionalSyntax(null, p1, p2, p3, p4, p5, p6, executor);
    }

    //#endregion

    public Command build(NeoFrameworkServer server) {
        construct(server);
        command.setDefaultExecutor((sender, _) ->
            sender.sendMessage(Messages.message("commands.missing_arguments")));
        return command;
    }
}
