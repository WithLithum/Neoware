/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.executors;

import net.minestom.server.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface CommandFunction3<T1, T2, T3> {
    boolean apply(CommandSender sender, T1 arg1, T2 arg2, T3 arg3);
}
