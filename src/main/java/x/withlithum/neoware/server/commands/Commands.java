package x.withlithum.neoware.server.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Contract;
import x.withlithum.neoware.server.commands.impl.AboutCommand;
import x.withlithum.neoware.server.commands.impl.KickCommand;
import x.withlithum.neoware.server.commands.impl.OpCommand;
import x.withlithum.neoware.server.commands.impl.StopCommand;

public final class Commands {
    public static void register() {
        var manager = MinecraftServer.getCommandManager();
        manager.register(new StopCommand(),
            new OpCommand(),
            new KickCommand(),
            new AboutCommand());
    }

    @Contract("null, _ -> false")
    public static boolean senderHasPermission(CommandSender sender, PermissionRank rank) {
        return (sender instanceof ConsoleSender)
            || (sender instanceof ServerSender)
            || (sender instanceof Player player && player.getPermissionLevel() >= rank.ordinal());
    }
}
