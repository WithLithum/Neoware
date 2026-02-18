/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.server.NeoServer;
import x.withlithum.neoware.server.commands.CommandArguments;
import x.withlithum.neoware.server.commands.CommandConditions;
import x.withlithum.neoware.server.commands.CommandSkeleton;
import x.withlithum.neoware.server.commands.Commands;
import x.withlithum.neoware.util.text.Messages;

@NullMarked
@Slf4j
public final class KickCommand extends CommandSkeleton {
    public KickCommand() {
        super("kick");
    }

    private final Component noReasonGiven = message("no_reason");

    @Override
    public void construct(NeoServer server) {
        conditionalSyntax(CommandConditions.AT_LEAST_MODERATOR,
            CommandArguments.singlePlayer("target"),
            ArgumentType.String("reason"),
            (sender, targetFinder, reason) -> {
                final var target = targetFinder.findFirstPlayer(sender);
                if (target == null) {
                    return failure(sender, Messages.ARGUMENT_PLAYER_NOT_FOUND);
                }

                final var reasonMessage = reason.isBlank()
                    ? noReasonGiven
                    : Component.text(reason);

                target.kick(message("disconnect", reasonMessage.decorate(TextDecoration.ITALIC)));
                log.info("{} kicked {} ({}) for reason: {}",
                    Commands.nameOf(sender),
                    Commands.nameOf(target),
                    target.getUuid(),
                    PlainTextComponentSerializer.plainText().serialize(reasonMessage));

                return succeed(sender, message("success", target.getName()));
            });
    }
}
