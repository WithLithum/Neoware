/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import x.withlithum.neoware.data.encode.codecs.NeowareCodecs;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Represents a single entry in the player blocklist.
 *
 * @param reason The reason of the ban. If {@code null}, no reason is displayed to the user.
 * @param until The time, in UTC, when the ban will expire. If {@code null}, the ban is permanent.
 *              Non-UTC time results in undefined behavior.
 */
public record PlayerBlocklistEntry(@Nullable String reason,
                                   @Nullable Instant until) {
    public static final Codec<PlayerBlocklistEntry> CODEC = StructCodec.struct(
        "reason", Codec.STRING.optional(), PlayerBlocklistEntry::reason,
        "until", NeowareCodecs.INSTANT_SECONDS.optional(), PlayerBlocklistEntry::until,
        PlayerBlocklistEntry::new
    );

    /**
     * Determines whether the ban have expired.
     * @return {@code true} if the ban have expired; otherwise, {@code false}.
     */
    public boolean hasExpired() {
        return until != null && until.isBefore(Instant.now());
    }
}
