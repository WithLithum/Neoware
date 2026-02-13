/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results

import io.github.oshai.kotlinlogging.KLogger
import net.minestom.server.codec.Result

/**
 * Reports the result of an operation that has a return value.
 */
sealed interface NeoResult<V> {
    companion object {
        @JvmStatic
        fun <V> fromMinestom(result: Result<V>): NeoResult<V> {
            return when (result) {
                is Result.Ok<V> -> Ok(result.value)
                is Result.Error<V> -> Error(result.message)
            }
        }
    }

    /**
     * A successful result containing a value.
     */
    data class Ok<V>(val value: V) : NeoResult<V> {
        override fun <R> map(func: (V) -> NeoResult<R>): NeoResult<R> {
            return func(value)
        }

        override fun unwrap(): V = value
    }

    /**
     * A failure result containing an error message and optionally a throwable that is responsible
     * for the failure.
     */
    data class Error<V>(val message: String, val cause: Throwable? = null) : NeoResult<V> {
        fun logWarn(logger: KLogger) {
            logger.warn(cause) { message }
        }

        fun <R> cast(): NeoResult<R> {
            return Error(message, cause)
        }

        override fun <R> map(func: (V) -> NeoResult<R>): NeoResult<R> {
            return cast()
        }

        override fun unwrap(): V {
            throw NeoResultException(message, cause)
        }
    }

    fun <R> map(func: (V) -> NeoResult<R>): NeoResult<R>

    /**
     * Asserts the current result is a successful result.
     *
     * @return The value contained within.
     * @throws [NeoResultException] The current result does not indicate success.
     */
    fun unwrap(): V
}