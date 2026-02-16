/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode.codecs;

import net.minestom.server.codec.Codec;

import java.time.Instant;

public final class NeowareCodecs {
    private NeowareCodecs() {
        throw new AssertionError("No NeowareCodecs instances for you!");
    }

    public static final Codec<Instant> INSTANT_SECONDS = new InstantCodecSecondsImpl();
}
