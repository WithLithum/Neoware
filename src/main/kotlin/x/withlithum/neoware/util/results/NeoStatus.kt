/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results

import io.github.oshai.kotlinlogging.KLogger

/**
 * Reports the result of an operation that has no return value.
 */
sealed interface NeoStatus {
    object Ok : NeoStatus {
        override fun unwrap() {
            // Intentionally do nothing since this is a successful result
        }

        override fun <V> withValue(value: V): NeoResult<V> {
            return NeoResult.Ok(value)
        }
    }

    data class Error(val message: String, val cause: Throwable? = null) : NeoStatus {
        fun logWarn(logger: KLogger) {
            logger.warn(cause) { message }
        }

        override fun unwrap() {
            throw NeoResultException(message, cause)
        }

        override fun <V> withValue(value: V): NeoResult<V> {
            return NeoResult.Error(message, cause)
        }
    }

    /**
     * Asserts this instance is a successful status.
     *
     * @throws [NeoResultException] The current status does not indicate success.
     */
    fun unwrap()

    /**
     * Returns a new corresponding instance of [NeoResult] with the specified value.
     */
    fun <V> withValue(value: V): NeoResult<V>
}