/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource

object ServerSettings {
    private var configData : ServerSettingsData? = null

    @JvmStatic
    val data
        get() = configData ?: throw IllegalStateException("Server settings not initialized")

    @JvmStatic
    fun init() {
        val configBinder = ConfigLoaderBuilder.default()
            .addResourceSource("settings.toml")
            .addFileSource("settings.toml")
            .build()
            .configBinder()

        configData = configBinder.bindOrThrow<ServerSettingsData>("neoware")
    }
}