package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import x.withlithum.neoware.server.commands.CommandMessages;
import x.withlithum.neoware.server.commands.NeoArguments;
import x.withlithum.neoware.server.commands.NeoCommand;
import x.withlithum.neoware.server.commands.condition.CommandConditions;

public class KickCommand extends NeoCommand {
    public KickCommand() {
        super("kick");
    }

    private static final Component KICK_MESSAGE = Component.translatable("multiplayer.disconnect.kicked");

    @Override
    public void construct() {
        addCondition(CommandConditions.IS_MODERATOR);

        var targetArgument = NeoArguments.onePlayer("target");

        addSyntax((sender, context) -> {
            if (!checkConditions(sender)) {
                failFor(context);
                return;
            }

            var targetFinder = context.get(targetArgument);

            var target = targetFinder.findFirstPlayer(sender);
            if (target == null) {
                failFor(context);
                sender.sendMessage(CommandMessages.NO_SUCH_PLAYER);
                return;
            }

            target.kick(KICK_MESSAGE);

        }, targetArgument);
    }
}
