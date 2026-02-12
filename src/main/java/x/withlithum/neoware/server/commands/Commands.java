/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Contract;
import x.withlithum.neoware.framework.server.NeoFrameworkServer;
import x.withlithum.neoware.server.commands.builtin.*;

public final class Commands {
    public static void register(NeoFrameworkServer server) {
        CommandFramework.INSTANCE.register(server,
            new FxStopCommand(),
            new FxAboutCommand(),
            new FxKickCommand(),
            new FxOpCommand(),
            new BanCommand(),
            new PardonCommand());
    }

    @Contract("null, _ -> false")
    public static boolean senderHasPermission(CommandSender sender, PermissionRank rank) {
        return (sender instanceof ConsoleSender)
            || (sender instanceof ServerSender)
            || (sender instanceof Player player && player.getPermissionLevel() >= rank.ordinal());
    }
}
