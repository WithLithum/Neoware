/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import net.minestom.server.codec.Codec
import java.io.InputStream

/**
 * Defines a means to load a content, of the specified type, from the specified stream.
 */
fun interface ContentLoader<V> {
    companion object {
        fun <V> toml(codec: Codec<V>): ContentLoader<V> {
            return TomlCodecContentLoader(codec)
        }
    }

    fun load(stream: InputStream): Result<V>
}