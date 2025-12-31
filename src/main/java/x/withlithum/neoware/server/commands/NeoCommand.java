package x.withlithum.neoware.server.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandData;
import x.withlithum.neoware.server.commands.condition.NeoCommandCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements various shared logic for built-in commands.
 */
public abstract class NeoCommand extends Command {
    private final List<NeoCommandCondition> conditions = new ArrayList<>();

    private static final Component INCOMPLETE_COMMAND = Component.translatable()
        .key("command.unknown.command")
        .color(NamedTextColor.RED)
        .build();

    private static final Component HERE = Component.translatable()
        .key("command.context.here")
        .color(NamedTextColor.RED)
        .build();

    public static final String DATA_RETURN_IS_SUCCESS = "return_code";

    public static void failFor(CommandContext context) {
        context.setReturnData(new CommandData()
            .set(DATA_RETURN_IS_SUCCESS, false));
    }

    public NeoCommand(String name, String... aliases) {
        super(name, aliases);

        construct();

        if (getDefaultExecutor() == null) {
            setDefaultExecutor(NeoCommand::defaultExecutor);
        }
    }

    public abstract void construct();

    protected void addCondition(NeoCommandCondition condition) {
        conditions.add(condition);
    }

    protected boolean checkConditions(CommandSender sender) {
        for (NeoCommandCondition condition : conditions) {
            if (!condition.verify(sender)) {
                condition.reportFailure(sender);
                return false;
            }
        }

        return true;
    }

    private static void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(INCOMPLETE_COMMAND);
        sender.sendMessage(Component.text()
            .content(context.getInput())
            .color(NamedTextColor.GRAY)
            .append(HERE));
    }
}
