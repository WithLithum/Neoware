/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import net.minestom.server.command.CommandSender;
import net.minestom.server.utils.entity.EntityFinder;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.server.NeoServer;
import x.withlithum.neoware.server.commands.*;
import x.withlithum.neoware.util.text.Messages;

@NullMarked
public final class OpCommand extends CommandSkeleton {
    public OpCommand() {
        super("op");
    }

    @Override
    public void construct(NeoServer server) {
        conditionalSyntax(CommandConditions.AT_LEAST_WHEEL,
            CommandArguments.singlePlayer("target"),
            this::execute);
    }

    private boolean execute(CommandSender sender,
                                   EntityFinder targetFinder) {
        final var target = targetFinder.findFirstPlayer(sender);
        if (target == null) {
            return failure(sender, Messages.ARGUMENT_PLAYER_NOT_FOUND);
        }

        target.setPermissionLevel(PermissionRank.SYS_OP.ordinal());

        return succeed(sender, message("success", target.getName()));
    };
}
