/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import net.minestom.server.command.builder.arguments.ArgumentType
import x.withlithum.neoware.server.NeoWareServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.server.commands.helpers.fail
import x.withlithum.neoware.server.commands.helpers.succeed

class PardonCommand : FxCommand("pardon") {
    override fun construct() {
        conditionalSyntax(FxConditions.IS_SYS_OP,
            ArgumentType.UUID("uuid")) cmd@{ sender, target ->
            val manager = NeoWareServer.INSTANCE.banManager

            if (!manager.isBanned(target)) {
                return@cmd fail(sender, lcMe("not_banned"))
            }

            manager.remove(target)
            return@cmd succeed(sender, lcMe("success"))
        }
    }
}