/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results

import io.github.oshai.kotlinlogging.KLogger

/**
 * Reports the result of an operation that has a return value.
 */
sealed interface NeoResult<V> {
    /**
     * A successful result containing a value.
     */
    data class Ok<V>(val value: V) : NeoResult<V> {
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

        override fun unwrap(): V {
            if (cause != null) {
                throw NeoResultException(message, cause)
            } else {
                throw NeoResultException(message)
            }
        }
    }

    /**
     * Asserts the current result is a successful result.
     *
     * @return The value contained within.
     * @throws [NeoResultException] The current result does not indicate success.
     */
    fun unwrap(): V
}