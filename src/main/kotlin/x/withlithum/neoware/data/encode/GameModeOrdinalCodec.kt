/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.minestom.server.codec.Codec
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import net.minestom.server.entity.GameMode

object GameModeOrdinalCodec : Codec<GameMode> {
    override fun <D> encode(
        coder: Transcoder<D>,
        value: GameMode?
    ): Result<D> {
        if (value == null) {
            return Result.Ok(coder.createNull())
        }

        return Result.Ok(coder.createInt(value.ordinal))
    }

    override fun <D> decode(
        coder: Transcoder<D>,
        value: D
    ): Result<GameMode> {
        return coder.getInt(value).map {
            if (GameMode.entries.size >= it || it < 0) {
                return@map Result.Error("Invalid game mode ordinal")
            }

            return@map Result.Ok(GameMode.entries[it])
        }
    }
}