/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.utils.entity.EntityFinder;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.server.NeoServer;
import x.withlithum.neoware.server.commands.CommandArguments;
import x.withlithum.neoware.server.commands.CommandConditions;
import x.withlithum.neoware.server.commands.CommandSkeleton;
import x.withlithum.neoware.server.commands.Commands;
import x.withlithum.neoware.util.Eval;
import x.withlithum.neoware.util.text.BannedMessage;
import x.withlithum.neoware.util.text.Messages;

import java.time.Duration;
import java.time.Instant;

@NullMarked
@Slf4j
public final class BanCommand extends CommandSkeleton {
    private static final Argument<EntityFinder> ARGUMENT_TARGET = CommandArguments.singlePlayer("target");
    private static final Argument<Duration> ARGUMENT_EXPIRY = ArgumentType.Time("expiry")
        .setDefaultValue(Duration.ZERO);
    private static final Argument<String> ARGUMENT_REASON = ArgumentType.String("reason")
        .setDefaultValue("");

    public BanCommand() {
        super("ban");
    }

    @Override
    public void construct(NeoServer server) {
        conditionalSyntax(
            CommandConditions.AT_LEAST_SYS_OP,
            ARGUMENT_TARGET,
            ARGUMENT_EXPIRY,
            ARGUMENT_REASON, (sender, targetFinder, expiry, reason) -> {
                final var target = targetFinder.findFirstPlayer(sender);
                if (target == null) {
                    return failure(sender, Messages.ARGUMENT_PLAYER_NOT_FOUND);
                }

                final var blocklist = server.playerBlocklist();

                if (blocklist.lookup(target.getUuid()) != null) {
                    return failure(sender, message("already_banned", target.getName()));
                }

                final var info = blocklist.insert(target.getUuid(),
                    reason.isBlank() ? null : reason,
                    expiry.isZero() ? null : Instant.now().plus(expiry));

                target.kick(BannedMessage.create(info));
                log.info("{} banned {} ({}) for reason: {}",
                    Commands.nameOf(sender),
                    Commands.nameOf(target),
                    target.getUuid(),
                    Eval.eitherText(reason, "No reason specified")
                );

                return succeed(sender, message("success", target.getName()));
            });
    }
}
