package x.withlithum.neoware.server.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Contract;
import x.withlithum.neoware.server.commands.builtin.FxAboutCommand;
import x.withlithum.neoware.server.commands.builtin.FxKickCommand;
import x.withlithum.neoware.server.commands.builtin.FxOpCommand;
import x.withlithum.neoware.server.commands.builtin.FxStopCommand;

public final class Commands {
    public static void register() {
        CommandFramework.INSTANCE.register(new FxStopCommand(),
            new FxAboutCommand(),
            new FxKickCommand(),
            new FxOpCommand());
    }

    @Contract("null, _ -> false")
    public static boolean senderHasPermission(CommandSender sender, PermissionRank rank) {
        return (sender instanceof ConsoleSender)
            || (sender instanceof ServerSender)
            || (sender instanceof Player player && player.getPermissionLevel() >= rank.ordinal());
    }
}
