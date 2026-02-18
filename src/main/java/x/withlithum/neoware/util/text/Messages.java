/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.text;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.util.Eval;

@NullMarked
public final class Messages {
    private Messages() {
        throw new AssertionError("No Messages instances for you!");
    }

    private static final String MESSAGE_KEY_PREFIX = "neoware";

    private static final Component ERROR_PREFIX = createPrefix(NamedTextColor.RED, NamedTextColor.DARK_RED);
    private static final Component SUCCESS_PREFIX = createPrefix(NamedTextColor.GREEN, null);
    private static final Component MESSAGE_PREFIX = createPrefix(NamedTextColor.GOLD, null);

    public static final Component ARGUMENT_PLAYER_NOT_FOUND = Component.translatable("neoware.arguments.no_player");

    private static Component createPrefix(TextColor color,
                                          @Nullable TextColor overrideMessageColor) {
        return Component.text()
            .color(Eval.either(overrideMessageColor, NamedTextColor.GREEN))
            .append(Component.text()
                .content("[")
                .color(NamedTextColor.DARK_GRAY))
            .append(Component.text()
                .content("!")
                .color(color))
            .append(Component.text()
                .content("] ")
                .color(NamedTextColor.DARK_GRAY))
            .build();
    }

    public static TranslatableComponent message(String key) {
        final var fullKey = String.format("%1$s.%2$s", MESSAGE_KEY_PREFIX, key);
        return Component.translatable(fullKey);
    }

    public static TranslatableComponent message(String root, String key) {
        final var fullKey = String.format("%1$s.%2$s", root, key);
        return Component.translatable(fullKey);
    }

    public static TranslatableComponent argMessage(String argType, String key) {
        final var fullKey = String.format("%1$s.arguments.%2$s.%3$s", MESSAGE_KEY_PREFIX, argType, key);
        return Component.translatable(fullKey);
    }

    public static void sendSuccess(Audience audience, Component message) {
        audience.sendMessage(SUCCESS_PREFIX.append(message));
    }

    public static void sendError(Audience audience, Component message) {
        audience.sendMessage(ERROR_PREFIX.append(message));
    }

    public static void sendMessage(Audience audience, Component message) {
        audience.sendMessage(MESSAGE_PREFIX.append(message));
    }
}
