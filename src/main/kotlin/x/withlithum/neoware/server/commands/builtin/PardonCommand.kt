/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import net.minestom.server.command.builder.arguments.ArgumentType
import x.withlithum.neoware.framework.server.NeoFrameworkServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.server.commands.helpers.fail
import x.withlithum.neoware.server.commands.helpers.succeed

class PardonCommand : FxCommand("pardon") {
    override fun construct(server: NeoFrameworkServer) {
        conditionalSyntax(FxConditions.IS_SYS_OP,
            ArgumentType.UUID("uuid")) cmd@{ sender, target ->
            val manager = server.banManager

            if (!manager.remove(target)) {
                return@cmd fail(sender, lcMe("not_banned"))
            }

            return@cmd succeed(sender, lcMe("success"))
        }
    }
}