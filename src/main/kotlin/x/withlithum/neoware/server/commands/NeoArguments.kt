/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands

import net.kyori.adventure.text.Component
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.ArgumentCallback
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.utils.entity.EntityFinder
import x.withlithum.neoware.util.messages.extensions.lc.lcArg
import x.withlithum.neoware.util.messages.extensions.sendNeoError

object NeoArguments {
    fun onePlayer(name: String): Argument<EntityFinder> {
        val type = ArgumentType.Entity(name)
            .singleEntity(true)
            .onlyPlayers(true)

        type.callback =
            ArgumentCallback { sender: CommandSender, exception: ArgumentSyntaxException ->
                sendIncorrectArguments(
                    sender, exception, when (exception.errorCode) {
                        ArgumentEntity.ONLY_PLAYERS_ERROR -> lcArg("player", "entities")
                        ArgumentEntity.ONLY_SINGLE_ENTITY_ERROR -> lcArg("entity", "too_many")
                        ArgumentEntity.INVALID_ARGUMENT_NAME -> lcArg("player", "name")
                        else -> null
                    }
                )
            }

        return type
    }

    private fun sendIncorrectArguments(
        sender: CommandSender,
        error: ArgumentSyntaxException,
        localisedMessage: Component?
    ) {
        sender.sendNeoError(
            localisedMessage
                ?: Component.text(error.message ?: error.errorCode.toString())
        )
    }
}