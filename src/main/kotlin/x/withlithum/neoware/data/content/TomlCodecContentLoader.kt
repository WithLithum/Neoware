/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import com.google.gson.JsonParseException
import io.github.wasabithumb.jtoml.JToml
import io.github.wasabithumb.jtoml.document.TomlDocument
import net.minestom.server.codec.Codec
import x.withlithum.neoware.data.encode.TomlTranscoder
import java.io.IOException
import java.io.InputStream

class TomlCodecContentLoader<V>(val codec: Codec<V>): ContentLoader<V> {
    companion object {
        private val TOML = JToml.jToml()
    }

    override val acceptsExtension = "toml"

    override fun load(stream: InputStream): Result<V> {
        val toml: TomlDocument
        try {
            toml = stream.use { stream ->
                stream.bufferedReader().use { reader ->
                    TOML.read(reader)
                }
            }
        } catch (ex: IOException) {
            return Result.failure(ex)
        } catch (ex: JsonParseException) {
            return Result.failure(ex)
        }

        return when (val result = codec.decode(TomlTranscoder.INSTANCE, toml)) {
            is net.minestom.server.codec.Result.Ok -> Result.success(result.value)
            is net.minestom.server.codec.Result.Error -> Result.failure(ContentException(result.message))
        }
    }
}