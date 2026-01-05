/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.builtin

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.command.builder.arguments.ArgumentType
import x.withlithum.neoware.server.NeoWareServer
import x.withlithum.neoware.server.commands.FxCommand
import x.withlithum.neoware.server.commands.FxConditions
import x.withlithum.neoware.server.commands.NeoArguments
import x.withlithum.neoware.server.commands.extensions.makeOptional
import x.withlithum.neoware.server.commands.helpers.fail
import x.withlithum.neoware.server.commands.helpers.succeed
import x.withlithum.neoware.util.messages.BanMessage
import x.withlithum.neoware.util.messages.extensions.lc.lcArg
import x.withlithum.neoware.util.messages.extensions.nameOf
import java.time.Instant
import kotlin.time.toKotlinInstant

class BanCommand : FxCommand("ban") {
    companion object {
        private val ARGUMENT_TARGET = NeoArguments.onePlayer("target")
        private val ARGUMENT_EXPIRY = ArgumentType.Time("expiry")
            .makeOptional()
        private val ARGUMENT_REASON = ArgumentType.String("reason")
            .makeOptional()

        private val LOG = KotlinLogging.logger {  }
    }

    override fun construct() {
        conditionalSyntax(
            FxConditions.IS_SYS_OP,
            ARGUMENT_TARGET,
            ARGUMENT_EXPIRY,
            ARGUMENT_REASON
        ) cmd@{ sender, targetFinder, expiry, reason ->
            val target = targetFinder.findFirstPlayer(sender) ?: return@cmd fail(
                sender,
                lcArg("player", "not_found")
            )
            if (NeoWareServer.INSTANCE.banManager.isBanned(target.uuid)) {
                return@cmd fail(sender, lcMe("already_banned", target.name))
            }

            val untilInstant: Instant? = if (expiry != null) {
                Instant.now().plus(expiry)
            } else {
                null
            }

            val info = NeoWareServer.INSTANCE.banManager.ban(target.uuid, reason,
                untilInstant?.toKotlinInstant()
            )
            target.kick(BanMessage.create(info))
            LOG.info { "${nameOf(sender)} banned ${target.username} (${target.uuid}) for reason: ${reason ?: "(No reason given)"}" }

            return@cmd succeed(sender, lcMe("success", target.name))
        }
    }
}