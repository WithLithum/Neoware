/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server

import io.github.oshai.kotlinlogging.KotlinLogging

object SaveAll {
    private val LOG = KotlinLogging.logger {}

    @JvmStatic
    fun save() {
        LOG.info { "Saving all game data" }

        NeoWareServer.INSTANCE.playerManager.saveAll()
        NeoWareServer.INSTANCE.banManager.saveList()
    }
}