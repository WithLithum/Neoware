/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import net.minestom.server.codec.Transcoder

class KJsonListBuilder : Transcoder.ListBuilder<JsonElement> {
    private val list: ArrayList<JsonElement>

    constructor(size: Int) {
        list = ArrayList(size)
    }

    override fun add(value: JsonElement): Transcoder.ListBuilder<JsonElement> {
        list.add(value)
        return this
    }

    override fun build(): JsonElement {
        return JsonArray(list)
    }
}