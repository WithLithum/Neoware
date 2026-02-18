/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.utils.entity.EntityFinder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.util.Eval;
import x.withlithum.neoware.util.text.Messages;

@NullMarked
public final class CommandArguments {
    private CommandArguments() {
        throw new AssertionError("No CommandArguments instances for you!");
    }

    public static Argument<EntityFinder> singlePlayer(String name) {
        final var type = ArgumentType.Entity(name)
            .singleEntity(true)
            .onlyPlayers(true);

        type.setCallback((sender, exception) ->
            sendIncorrectArguments(sender, exception, switch (exception.getErrorCode()) {
            case ArgumentEntity.ONLY_PLAYERS_ERROR -> Messages.argMessage("player", "entities");
            case ArgumentEntity.ONLY_SINGLE_ENTITY_ERROR -> Messages.argMessage("entity", "too_many");
            case ArgumentEntity.INVALID_ARGUMENT_NAME -> Messages.argMessage("player", "name");
            default -> null;
        }));

        return type;
    }

    private static void sendIncorrectArguments(CommandSender sender,
                                               ArgumentSyntaxException error,
                                               @Nullable Component localizedMessage) {
        Messages.sendError(sender,
            Eval.either(localizedMessage,
                Component.text(Eval.either(error.getMessage(), Integer.toString(error.getErrorCode())))));
    }
}
