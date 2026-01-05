/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.minestom.server.codec.Codec
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import java.util.UUID

object StringUuidCodec : Codec<UUID> {
    override fun <D> encode(
        coder: Transcoder<D>,
        value: UUID?
    ): Result<D> {
        if (value == null) {
            return Result.Ok(coder.createNull())
        }

        return Result.Ok(coder.createString(value.toString()))
    }

    override fun <D> decode(
        coder: Transcoder<D>,
        value: D?
    ): Result<UUID?> {
        if (value == null) {
            return Result.Ok(null)
        }

        return coder.getString(value).map {
            try {
                return@map Result.Ok(UUID.fromString(it))
            } catch (_: IllegalArgumentException) {
                return@map Result.Error("Invalid UUID")
            }
        }
    }
}