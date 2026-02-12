/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.server.config

import x.withlithum.neoware.server.config.ServerSettings

data class AdventureServerSettings(val level: String? = null) {
    companion object {
        private var configData : AdventureServerSettings? = null

        val data
            get() = configData ?: throw IllegalStateException("Server settings not initialized")

        fun init() {
            if (configData != null) {
                throw IllegalStateException("Server settings already initialized")
            }

            ServerSettings.init()
            configData = ServerSettings.bind("adventure")
        }
    }


}