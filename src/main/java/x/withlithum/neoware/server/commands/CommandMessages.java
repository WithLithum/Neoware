package x.withlithum.neoware.server.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class CommandMessages {
    public static final Component NO_SUCH_PLAYER = Component.translatable()
        .key("argument.player.unknown")
        .color(NamedTextColor.RED)
        .build();
}
