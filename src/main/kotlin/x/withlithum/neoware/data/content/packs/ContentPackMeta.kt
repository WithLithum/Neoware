/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs

import net.minestom.server.codec.Codec
import net.minestom.server.codec.StructCodec

data class ContentPackMeta(
    val name: String,
    val dataVersion: Int
) {
    companion object {
        val CODEC: Codec<ContentPackMeta> =
            StructCodec.struct(
                "name", Codec.STRING, ContentPackMeta::name,
                "data_version", Codec.INT, ContentPackMeta::dataVersion,
                ::ContentPackMeta
            )
    }
}
