/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.math;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public record Vector2F(float x,
                       float y) {

    public static final Codec<Vector2F> CODEC = new Codec<>() {
        @Override
        public <D> Result<Vector2F> decode(Transcoder<D> coder, D value) {
            final var listResult = coder.getList(value);
            if (!(listResult instanceof Result.Ok<List<D>>(List<D> list))) {
                return listResult.cast();
            }

            if (list.size() != 2) {
                return new Result.Error<>("Invalid length for Vector2, expected 2 but got " + list.size());
            }

            final var xE = coder.getFloat(list.get(0));
            final var yE = coder.getFloat(list.get(1));
            if (!(xE instanceof Result.Ok<Float>(Float x))) {
                return xE.cast();
            }
            if (!(yE instanceof Result.Ok<Float>(Float y))) {
                return yE.cast();
            }

            return new Result.Ok<>(new Vector2F(x, y));
        }

        @Override
        public <D> Result<D> encode(Transcoder<D> coder, @Nullable Vector2F value) {
            if (value == null) {
                return new Result.Ok<>(coder.createNull());
            }

            final var list = coder.createList(2);
            list.add(coder.createFloat(value.x()));
            list.add(coder.createFloat(value.y()));
            return new Result.Ok<>(list.build());
        }
    };
}
