/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.framework.server.NeoFrameworkServer;
import x.withlithum.neoware.server.commands.CommandSkeleton;
import x.withlithum.neoware.util.text.Messages;

@NullMarked
public final class AboutCommand extends CommandSkeleton {
    private final Component copyrightMessage = message("copyright");

    private final Component minestomMessage = message(
        "minestom", Component.text()
            .content(MinecraftServer.VERSION_NAME)
            .color(NamedTextColor.YELLOW)
            .build()
        ).color(NamedTextColor.GRAY);

    private final Component sourceMessage = message("source_link")
        .color(NamedTextColor.BLUE)
        .decorate(TextDecoration.UNDERLINED)
        .clickEvent(ClickEvent.openUrl("https://gitea.com/WithLithum/neoware"));

    public AboutCommand() {
        super("about");
    }

    @Override
    public void construct(NeoFrameworkServer server) {
        syntax(this::execute);
    }

    private boolean execute(CommandSender sender) {
        Messages.sendMessage(sender, copyrightMessage);
        sender.sendMessage(minestomMessage);
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text()
            .content("[")
            .color(NamedTextColor.DARK_GRAY)
            .append(sourceMessage)
            .append(Component.text("]")));

        return true;
    }
}
