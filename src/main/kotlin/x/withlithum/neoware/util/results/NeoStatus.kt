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
    }

    data class Error(val message: String, val cause: Throwable? = null) : NeoStatus {
        fun logWarn(logger: KLogger) {
            logger.warn(cause) { message }
        }

        override fun unwrap() {
            if (cause != null) {
                throw NeoResultException(message, cause)
            } else {
                throw NeoResultException(message)
            }
        }
    }

    /**
     * Asserts this instance is a successful status.
     *
     * @throws [NeoResultException] The current status does not indicate success.
     */
    fun unwrap()
}