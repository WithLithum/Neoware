package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import x.withlithum.neoware.server.NeoWareServer;
import x.withlithum.neoware.server.commands.condition.CommandConditions;
import x.withlithum.neoware.server.commands.NeoCommand;
import x.withlithum.neoware.server.commands.PermissionRank;

/**
 * Stops the NeoWare server. This command requires {@link PermissionRank#WHEEL} permission.
 */
public final class StopCommand extends NeoCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void construct() {
        addCondition(CommandConditions.IS_WHEEL);

        setDefaultExecutor((sender, args) -> {
            if (!checkConditions(sender)) {
                failFor(args);
                return;
            }

            MinecraftServer.getSchedulerManager().scheduleNextTick(NeoWareServer.INSTANCE::stop);
            sender.sendMessage(Component.translatable("commands.stop.stopping"));
        });
    }
}
