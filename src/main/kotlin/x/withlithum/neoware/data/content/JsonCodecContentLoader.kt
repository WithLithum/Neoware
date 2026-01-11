/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import net.minestom.server.codec.Codec
import net.minestom.server.codec.Transcoder
import java.io.IOException
import java.io.InputStream

/**
 * Loads JSON content from a stream with the specified codec.
 */
class JsonCodecContentLoader<V>(val codec: Codec<V>): ContentLoader<V> {
    override fun load(stream: InputStream): Result<V> {
        val json: JsonElement
        try {
            json = stream.use { stream ->
                stream.bufferedReader().use { reader ->
                    JsonParser.parseReader(reader)
                }
            }
        } catch (ex: IOException) {
            return Result.failure(ex)
        } catch (ex: JsonParseException) {
            return Result.failure(ex)
        }

        return when (val result = codec.decode(Transcoder.JSON, json)) {
            is net.minestom.server.codec.Result.Ok -> Result.success(result.value)
            is net.minestom.server.codec.Result.Error -> Result.failure(ContentException(result.message))
        }
    }

}