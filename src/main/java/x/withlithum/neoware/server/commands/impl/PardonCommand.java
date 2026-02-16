/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.server.NeoServer;
import x.withlithum.neoware.server.commands.CommandConditions;
import x.withlithum.neoware.server.commands.CommandSkeleton;

import java.util.UUID;

@NullMarked
public final class PardonCommand extends CommandSkeleton {
    public PardonCommand() {
        super("pardon");
    }

    @Override
    public void construct(NeoServer server) {
        conditionalSyntax(CommandConditions.AT_LEAST_SYS_OP,
            ArgumentType.UUID("uuid"),
            (sender, target) -> execute(server, sender, target));
    }

    private boolean execute(NeoServer server, CommandSender sender, UUID target) {
        final var manager = server.playerBlocklist();

        if (!manager.remove(target)) {
            return failure(sender, message("not_banned"));
        }

        return succeed(sender, message("success"));
    }
}
