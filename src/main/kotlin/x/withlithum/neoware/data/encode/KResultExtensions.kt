/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import net.minestom.server.codec.Result

fun <V> errExpectedNumber(): Result<V> {
    return Result.Error("Expected a JSON number")
}

inline fun <V, R> Result<V>.kMap(f: (V) -> Result<R>): Result<R> {
    if (this !is Result.Ok) {
        return this.cast<R>()
    }

    return f(this.value)
}

/**
 * If the current result is [Result.Ok], performs the specified mapping function and wraps its
 * result in [Result.Ok]. Otherwise, returns the current result.
 *
 * This is a Kotlin inline version of [Result.mapResult].
 *
 * @param f The mapping function.
 */
inline fun <V, R> Result<V>.kMapResult(f: (V) -> R): Result<R> {
    if (this !is Result.Ok) {
        return this.cast<R>()
    }

    return Result.Ok(f(this.value))
}