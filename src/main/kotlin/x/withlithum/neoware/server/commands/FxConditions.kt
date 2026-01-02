/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands

import net.minestom.server.command.builder.condition.CommandCondition

object FxConditions {
    val IS_MEMBER = createRank(PermissionRank.MEMBER)
    val IS_MODERATOR = createRank(PermissionRank.MODERATOR)
    val IS_SYS_OP = createRank(PermissionRank.SYS_OP)
    val IS_WHEEL = createRank(PermissionRank.WHEEL)

    private fun createRank(rank: PermissionRank): CommandCondition {
        return { sender -> Commands.senderHasPermission(sender, rank) }
    }
}