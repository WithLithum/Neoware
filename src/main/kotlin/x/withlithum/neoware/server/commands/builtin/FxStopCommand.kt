/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import net.minestom.server.MinecraftServer
import x.withlithum.neoware.framework.server.NeoFrameworkServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.util.messages.extensions.sendNeoSuccess

class FxStopCommand : FxCommand("stop") {
    private val successMessage = lcMe("stopping")

    override fun construct(server: NeoFrameworkServer) {
        conditionalSyntax(FxConditions.IS_WHEEL) { sender ->
            sender.sendNeoSuccess(successMessage)
            MinecraftServer.getSchedulerManager().scheduleNextTick {
                server.stop()
            }

            return@conditionalSyntax true
        }
    }
}