/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import com.google.gson.JsonParseException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import net.minestom.server.codec.Codec
import x.withlithum.neoware.data.encode.KJsonTranscoder
import java.io.IOException
import java.io.InputStream

/**
 * Loads JSON content from a stream with the specified codec.
 */
@OptIn(ExperimentalSerializationApi::class)
class JsonCodecContentLoader<V>(val codec: Codec<V>): ContentLoader<V> {
    override fun load(stream: InputStream): Result<V> {
        val json: JsonElement
        try {
            json = stream.use { stream ->
                Json.decodeFromStream<JsonElement>(stream)
            }
        } catch (ex: IOException) {
            return Result.failure(ex)
        } catch (ex: JsonParseException) {
            return Result.failure(ex)
        }

        return when (val result = codec.decode(KJsonTranscoder, json)) {
            is net.minestom.server.codec.Result.Ok -> Result.success(result.value)
            is net.minestom.server.codec.Result.Error -> Result.failure(ContentException(result.message))
        }
    }
}