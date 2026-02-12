/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server

import io.github.oshai.kotlinlogging.KotlinLogging

object SaveAll {
    private val LOG = KotlinLogging.logger {}

    @Deprecated(message = "Perform save on NeoFrameworkServer instead.")
    @JvmStatic
    fun save() {
        LOG.warn { "Terminally deprecated function has been called" }
        LOG.warn { "SaveAll.save() no longer does anything. Use NeoFrameworkServer.save() instead." }
    }
}