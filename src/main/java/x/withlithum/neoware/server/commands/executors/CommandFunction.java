/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.executors;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import org.jspecify.annotations.NullMarked;

@FunctionalInterface
@NullMarked
public interface CommandFunction {
    boolean apply(CommandSender sender, CommandContext context);
}
