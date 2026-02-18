/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.minestom.server.command.builder.condition.CommandCondition;

public final class CommandConditions {
    private CommandConditions() {
        throw new AssertionError("No CommandConditions instances for you!");
    }

    public static final CommandCondition AT_LEAST_WHEEL = createAtLeastRank(PermissionRank.WHEEL);
    public static final CommandCondition AT_LEAST_SYS_OP = createAtLeastRank(PermissionRank.SYS_OP);
    public static final CommandCondition AT_LEAST_MODERATOR = createAtLeastRank(PermissionRank.MODERATOR);

    private static CommandCondition createAtLeastRank(PermissionRank rank) {
        return (sender, _) -> Commands.senderHasPermission(sender, rank);
    }
}
