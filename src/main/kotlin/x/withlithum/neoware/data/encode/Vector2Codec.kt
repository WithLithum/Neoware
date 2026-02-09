/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.minestom.server.codec.Codec
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import x.withlithum.neoware.data.storage.Vector2

object Vector2Codec : Codec<Vector2> {
    override fun <D> encode(
        coder: Transcoder<D>,
        value: Vector2?
    ): Result<D> {
        if (value == null) {
            return Result.Ok(value)
        }

        val list = coder.createList(2)
        list.add(coder.createDouble(value.x))
        list.add(coder.createDouble(value.y))

        return Result.Ok(list.build())
    }

    override fun <D> decode(
        coder: Transcoder<D>,
        value: D
    ): Result<Vector2> {
        val listResult = coder.getList(value)
        if (listResult !is Result.Ok) {
            return listResult.cast()
        }

        val list = listResult.value
        if (list.size != 2) {
            return Result.Error("Invalid length for Vector2, expected 2 but got ${list.size}");
        }

        val xResult = coder.getDouble(list[0])
        if (xResult !is Result.Ok) {
            return xResult.cast()
        }

        val yResult = coder.getDouble(list[1])
        if (yResult !is Result.Ok) {
            return yResult.cast()
        }

        return Result.Ok(Vector2(xResult.value, yResult.value))
    }

}