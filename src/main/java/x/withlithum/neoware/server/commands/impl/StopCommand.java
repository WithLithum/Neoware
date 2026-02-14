/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.framework.server.NeoFrameworkServer;
import x.withlithum.neoware.server.commands.CommandConditions;
import x.withlithum.neoware.server.commands.CommandSkeleton;

@NullMarked
public final class StopCommand extends CommandSkeleton {
    public StopCommand() {
        super("stop");
    }

    private final Component successMessage = message("stopping");

    @Override
    public void construct(NeoFrameworkServer server) {
        conditionalSyntax(CommandConditions.AT_LEAST_WHEEL,
            (sender) -> {
                MinecraftServer.getSchedulerManager().scheduleNextTick(server::stop);
                return succeed(sender, successMessage);
            }
        );
    }
}
