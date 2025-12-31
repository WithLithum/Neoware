package x.withlithum.neoware.server.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.utils.entity.EntityFinder;
import org.jspecify.annotations.Nullable;

public class NeoArguments {
    public static Argument<EntityFinder> onePlayer(String name) {
        var type = ArgumentType.Entity(name)
            .singleEntity(true)
            .onlyPlayers(true);

        type.setCallback((sender, exception) ->
            sendIncorrectArguments(sender, exception, switch (exception.getErrorCode()) {
                case ArgumentEntity.ONLY_PLAYERS_ERROR -> "argument.player.entities";
                case ArgumentEntity.ONLY_SINGLE_ENTITY_ERROR -> "argument.player.toomany";
                case ArgumentEntity.INVALID_ARGUMENT_NAME -> "argument.player.name";
                default -> null;
            }));

        return type;
    }

    private static void sendIncorrectArguments(CommandSender sender,
                                              ArgumentSyntaxException error,
                                              @Nullable String localisedMessage) {
        if (localisedMessage == null) {
            sender.sendMessage(Component.text(error.getMessage()).color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.translatable(localisedMessage).color(NamedTextColor.RED));
    }
}
