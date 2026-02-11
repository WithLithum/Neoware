/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.main

import okio.Path.Companion.toPath
import x.withlithum.neoware.adventure.server.NeoAdventureServer
import x.withlithum.neoware.framework.server.ConsoleUtil
import x.withlithum.neoware.server.config.Configs

fun main() {
    // Start server
    Configs.initialize()
    val server = NeoAdventureServer(System.getProperty("user.dir").toPath())
    server.start()

    val consoleThread = ConsoleUtil.createConsole(server)
    consoleThread.start()
}