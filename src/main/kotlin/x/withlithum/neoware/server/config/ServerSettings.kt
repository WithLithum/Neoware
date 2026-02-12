/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.config

import com.sksamuel.hoplite.ConfigBinder
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource

object ServerSettings {
    private var configData : ServerSettingsData? = null
    private var configBinder : ConfigBinder? = null

    @JvmStatic
    val data
        get() = configData ?: throw IllegalStateException("Server settings not initialized")

    @JvmStatic
    fun init() {
        configBinder = createBinder()

        configData = configBinder!!.bindOrThrow<ServerSettingsData>("neoware")
    }

    fun <V> bind(prefix: String): V {
        val configBinder = this.configBinder ?: throw IllegalStateException("Server settings not initialized");

        return configBinder.bindOrThrow(prefix)
    }

    private fun createBinder(): ConfigBinder {
        return ConfigLoaderBuilder.default()
            .addResourceSource("/settings.toml")
            .addFileSource("settings.toml", true)
            .build()
            .configBinder()
    }
}