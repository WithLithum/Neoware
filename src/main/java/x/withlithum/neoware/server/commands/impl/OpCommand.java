package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import x.withlithum.neoware.server.commands.condition.CommandConditions;
import x.withlithum.neoware.server.commands.NeoArguments;
import x.withlithum.neoware.server.commands.NeoCommand;
import x.withlithum.neoware.server.commands.PermissionRank;

/**
 * Promotes a player to {@link PermissionRank#SYS_OP}. This command requires the
 * {@link PermissionRank#WHEEL} permission.
 */
public final class OpCommand extends NeoCommand {
    public OpCommand() {
        super("op");
    }

    @Override
    public void construct() {
        addCondition(CommandConditions.IS_WHEEL);

        var targetArgument = NeoArguments.onePlayer("target");
        addSyntax((sender, context) -> {
            if (!checkConditions(sender)) {
                failFor(context);
                return;
            }

            var targetSelector = context.get(targetArgument);
            var target = targetSelector.findFirstPlayer(sender);
            if (target == null) {
                failFor(context);
                sender.sendMessage(Component.translatable("argument.player.unknown")
                    .color(NamedTextColor.RED));
                return;
            }

            target.setPermissionLevel(PermissionRank.SYS_OP.ordinal());
        }, targetArgument);
    }
}
