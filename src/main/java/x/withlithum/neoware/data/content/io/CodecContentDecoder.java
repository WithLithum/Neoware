/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Transcoder;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.InputStream;

/**
 * Provides a skeleton implementation for decoding encoded contents from a stream via a codec and
 * a transcoder.
 *
 * @param <V> The type of the resulting decoded value.
 * @param <E> The "element" type that is accepted by the {@link Transcoder}.
 */
public abstract class CodecContentDecoder<V, E> implements ContentDecoder<V> {
    protected final Codec<V> codec;
    protected final Transcoder<E> transcoder;

    protected CodecContentDecoder(Codec<V> codec, Transcoder<E> transcoder) {
        this.codec = codec;
        this.transcoder = transcoder;
    }

    public abstract NeoResult<E> decodeElement(InputStream stream);

    @Override
    public NeoResult<V> load(InputStream stream) {
        final var elementResult = decodeElement(stream);
        return switch (elementResult) {
            case NeoResult.Error<E> e -> e.cast();
            case NeoResult.Ok<E> o -> NeoResult.fromMinestom(codec.decode(transcoder, o.getValue()));
        };
    }
}
