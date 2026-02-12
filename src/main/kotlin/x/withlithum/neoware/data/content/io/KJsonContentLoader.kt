/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import x.withlithum.neoware.data.content.ContentLoader
import java.io.IOException
import java.io.InputStream

class KJsonContentLoader<V>(val serializer: KSerializer<V>) : ContentLoader<V> {
    @OptIn(ExperimentalSerializationApi::class)
    override fun load(stream: InputStream): Result<V> {
        return try {
            Result.success(Json.decodeFromStream(serializer, stream))
        } catch (e: Exception) {
            when (e) {
                is SerializationException -> Result.failure(e)
                is IllegalArgumentException -> Result.failure(e)
                is IOException -> Result.failure(e)
                else -> throw e
            }
        }
    }
}