package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import x.withlithum.neoware.server.commands.NeoCommand;

public class AboutCommand extends NeoCommand {
    private static final Component ABOUT_HEADER = Component.text("Running 'NeoWare' server");
    private static final Component ABOUT_DATA_VERSION = Component.text("Powered by Minestom for Minecraft ")
        .append(Component.text()
            .content(MinecraftServer.VERSION_NAME)
            .color(NamedTextColor.YELLOW))
        .append(Component.text(" (protocol "))
        .append(Component.text()
            .content(Integer.toString(MinecraftServer.PROTOCOL_VERSION))
            .color(NamedTextColor.LIGHT_PURPLE))
        .append(Component.text(", data "))
        .append(Component.text()
            .content(Integer.toString(MinecraftServer.DATA_VERSION))
            .color(NamedTextColor.AQUA))
        .append(Component.text(")"));
    private static final Component ABOUT_COPYRIGHT = Component.text("Copyright (C) 2025 WithLithum & contributors");

    public AboutCommand() {
        super("about");
    }

    @Override
    public void construct() {
        addSyntax((sender, _) -> {
            sender.sendMessage(ABOUT_HEADER);
            sender.sendMessage(ABOUT_COPYRIGHT);
            sender.sendMessage(ABOUT_DATA_VERSION);
        });
    }
}
