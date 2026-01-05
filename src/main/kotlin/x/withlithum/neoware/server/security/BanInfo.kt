/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.security

import net.minestom.server.codec.Codec
import net.minestom.server.codec.StructCodec
import x.withlithum.neoware.data.encode.InstantCodec
import kotlin.time.Instant

data class BanInfo(val reason: String?,
                   val from: Instant,
                   val to: Instant?) {
    companion object {
        val CODEC: StructCodec<BanInfo> = StructCodec.struct(
            "reason", Codec.STRING.optional(), BanInfo::reason,
            "from", InstantCodec, BanInfo::from,
            "to", InstantCodec.optional(), BanInfo::to,
            ::BanInfo
        )
    }
}
