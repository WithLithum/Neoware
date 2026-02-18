/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode.codecs;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.DateTimeException;
import java.time.Instant;

@NullMarked
final class InstantCodecSecondsImpl implements Codec<Instant> {
    @Override
    public <D> Result<Instant> decode(Transcoder<D> coder, D value) {
        return coder.getLong(value).map(l -> {
            try {
                return new Result.Ok<>(Instant.ofEpochSecond(l));
            } catch (DateTimeException e) {
                return new Result.Error<>(e.getMessage());
            }
        });
    }

    @Override
    public <D> Result<D> encode(Transcoder<D> coder, @Nullable Instant value) {
        if (value == null) {
            return new Result.Error<>("null");
        }

        return new Result.Ok<>(coder.createLong(value.getEpochSecond()));
    }
}
