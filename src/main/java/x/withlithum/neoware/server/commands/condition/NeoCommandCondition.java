package x.withlithum.neoware.server.commands.condition;

import net.minestom.server.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface NeoCommandCondition {
    boolean verify(@NotNull CommandSender sender);

    void reportFailure(CommandSender sender);
}
