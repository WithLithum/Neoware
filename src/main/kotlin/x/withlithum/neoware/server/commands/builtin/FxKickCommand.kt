/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.arguments.ArgumentType
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.server.commands.NeoArguments
import x.withlithum.neoware.server.commands.helpers.fail
import x.withlithum.neoware.server.commands.helpers.succeed
import x.withlithum.neoware.util.messages.NeoMessages
import x.withlithum.neoware.util.messages.extensions.nameOf

class FxKickCommand : FxCommand("kick") {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override fun construct() {
        conditionalSyntax(
            FxConditions.IS_MODERATOR,
            NeoArguments.onePlayer("target"),
            ArgumentType.String("reason")
        ) cmd@{ sender, targetFinder, reason ->
            val target = targetFinder.findFirstPlayer(sender)
                ?: return@cmd fail(sender, NeoMessages.ARGUMENT_PLAYER_NOT_FOUND)

            logger.info { "${nameOf(sender)} kicked player ${target.username} for reason $reason" }
            target.kick(lcMe("disconnect", Component.text(reason)))
            succeed(sender, lcMe("success", target.name))
        }
    }
}