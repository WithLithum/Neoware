/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import org.jetbrains.annotations.Unmodifiable

class KJsonMapLike(private val map: JsonObject) : Transcoder.MapLike<JsonElement> {
    override fun keys(): @Unmodifiable Collection<String> {
        return map.keys
    }

    override fun hasValue(key: String): Boolean {
        return map.contains(key)
    }

    override fun getValue(key: String): Result<JsonElement> {
        val result = map[key] ?: return Result.Error("Missing '$key' in JSON map-like")
        return Result.Ok(result)
    }
}