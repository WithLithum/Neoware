/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import net.minestom.server.codec.Transcoder

class KJsonMapBuilder : Transcoder.MapBuilder<JsonElement> {
    private val map = mutableMapOf<String, JsonElement>()

    override fun put(
        key: JsonElement,
        value: JsonElement
    ): Transcoder.MapBuilder<JsonElement> {
        if (key !is JsonPrimitive) {
            throw IllegalArgumentException("key must be a JSON primitive")
        }

        map[key.content] = value
        return this
    }

    override fun put(
        key: String,
        value: JsonElement
    ): Transcoder.MapBuilder<JsonElement> {
        map[key] = value
        return this
    }

    override fun build(): JsonElement {
        return JsonObject(map)
    }
}