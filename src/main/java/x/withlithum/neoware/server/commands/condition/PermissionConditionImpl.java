package x.withlithum.neoware.server.commands.condition;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import x.withlithum.neoware.server.commands.Commands;
import x.withlithum.neoware.server.commands.PermissionRank;

final class PermissionConditionImpl implements NeoCommandCondition {
    private static final Component MESSAGE_NO_PERMISSION = Component.translatable()
        .key("neo.commands.no_permission")
        .fallback("Insufficient permissions")
        .color(NamedTextColor.RED)
        .build();

    private final PermissionRank rank;

    PermissionConditionImpl(PermissionRank rank) {
        this.rank = rank;
    }

    @Override
    public boolean verify(@NotNull CommandSender sender) {
        return Commands.senderHasPermission(sender, rank);
    }

    @Override
    public void reportFailure(CommandSender sender) {
        sender.sendMessage(MESSAGE_NO_PERMISSION);
    }
}
