/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.minestom.server.codec.Codec
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import kotlin.time.Instant

object InstantCodec : Codec<Instant> {
    override fun <D> encode(
        coder: Transcoder<D>,
        value: Instant?
    ): Result<D> {
        return if (value == null) {
            Result.Ok(coder.createNull())
        } else {
            Result.Ok(coder.createLong(value.toEpochMilliseconds()))
        }
    }

    override fun <D> decode(
        coder: Transcoder<D>,
        value: D?
    ): Result<Instant?> {
        return if (value == null) {
            Result.Ok(null)
        } else {
            coder.getLong(value).mapResult { Instant.fromEpochMilliseconds(it) }
        }
    }
}