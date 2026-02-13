/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Transcoder;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class GsonCodecContentDecoder<V> extends CodecContentDecoder<V, JsonElement> {
    private static final Gson GSON = new Gson();

    public GsonCodecContentDecoder(Codec<V> codec) {
        super(codec, Transcoder.JSON);
    }

    @Override
    public NeoResult<JsonElement> decodeElement(InputStream stream) {
        try (var reader = GSON.newJsonReader(new BufferedReader(new InputStreamReader(stream)))) {
            return new NeoResult.Ok<>(JsonParser.parseReader(reader));
        } catch (JsonSyntaxException | IOException e) {
            return new NeoResult.Error<>("Failed to decode JSON", e);
        }
    }

    @Override
    public String getAcceptedExtension() {
        return "json";
    }
}
