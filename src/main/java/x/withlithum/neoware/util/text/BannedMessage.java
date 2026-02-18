/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import x.withlithum.neoware.server.player.PlayerBlocklistEntry;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class BannedMessage {
    private static final String BANNED_KEY = "multiplayer.disconnect.banned";
    private static final String BANNED_WITH_REASON_KEY = "multiplayer.disconnect.banned.reason";
    private static final String EXPIRATION_KEY = "multiplayer.disconnect.banned.expiration";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        .withZone(ZoneOffset.UTC);

    private BannedMessage() {
        throw new AssertionError("No BannedMessage instances for you!");
    }

    public static Component create(PlayerBlocklistEntry info) {
        final var reason = info.reason();
        var message = reason != null && !reason.isBlank()
            ? Component.translatable(BANNED_WITH_REASON_KEY, Component.text(reason).decorate(TextDecoration.ITALIC))
            : Component.translatable(BANNED_KEY);

        final var to = info.until();
        if (to != null) {
            // TODO use proper formatter
            message = message.append(Component.translatable(EXPIRATION_KEY, Component.text(
                FORMATTER.format(to)
            )));
        }

        return message;
    }
}
