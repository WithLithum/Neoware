/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Contract;
import x.withlithum.neoware.server.NeoServer;
import x.withlithum.neoware.server.commands.impl.*;
import x.withlithum.neoware.util.Eval;
import x.withlithum.neoware.util.text.Messages;

public final class Commands {
    private static final TranslatableComponent UNKNOWN_COMMAND_MESSAGE = Component.translatable()
        .key("neoware.commands.unknown")
        .color(NamedTextColor.RED)
        .build();

    public static void register(NeoServer server,
                                CommandBuildable... commands) {
        final var manager = MinecraftServer.getCommandManager();

        for (var command : commands) {
            manager.register(command.build(server));
        }
    }

    public static void init() {
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) ->
            Messages.sendError(sender,
                UNKNOWN_COMMAND_MESSAGE.arguments(
                    Component.text()
                        .content(command)
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.BOLD, false)
                )));
    }

    public static void register(NeoServer server) {
        register(server,
            new AboutCommand(),
            new StopCommand(),
            new OpCommand(),
            new PardonCommand(),
            new BanCommand(),
            new KickCommand());
    }

    public static String nameOf(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getUsername();
        } else if (sender instanceof Entity entity) {
            final var customName = entity.get(DataComponents.CUSTOM_NAME);

            return customName != null
                ? PlainTextComponentSerializer.plainText().serialize(customName)
                : entity.getEntityType().name();
        }

        return switch (sender) {
            case ConsoleSender _ -> "<!CONSOLE>";
            case ServerSender _ -> "<!Server>";
            default -> Eval.either(sender.getClass().getSimpleName(), "*UNKNOWN*");
        };
    }

    @Contract("null, _ -> false")
    public static boolean senderHasPermission(CommandSender sender, PermissionRank rank) {
        return (sender instanceof ConsoleSender)
            || (sender instanceof ServerSender)
            || (sender instanceof Player player && player.getPermissionLevel() >= rank.getValue());
    }
}
