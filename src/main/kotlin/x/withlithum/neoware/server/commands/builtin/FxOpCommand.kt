/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import x.withlithum.neoware.framework.server.NeoFrameworkServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.server.commands.NeoArguments
import x.withlithum.neoware.server.commands.PermissionRank
import x.withlithum.neoware.server.commands.helpers.fail
import x.withlithum.neoware.server.commands.helpers.succeed
import x.withlithum.neoware.util.messages.NeoMessages

class FxOpCommand : FxCommand("op") {
    override fun construct(server: NeoFrameworkServer) {
        conditionalSyntax(
            FxConditions.IS_WHEEL,
            NeoArguments.onePlayer("target")
        ) cmd@{ sender, targetFinder ->
            val target = targetFinder.findFirstPlayer(sender)
                ?: return@cmd fail(sender, NeoMessages.ARGUMENT_PLAYER_NOT_FOUND)

            target.permissionLevel = PermissionRank.SYS_OP.ordinal

            return@cmd succeed(sender, lcMe("success", target.name))
        }
    }
}