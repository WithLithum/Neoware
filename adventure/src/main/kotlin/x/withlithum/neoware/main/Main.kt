/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.main

import okio.Path.Companion.toPath
import x.withlithum.neoware.adventure.server.NeoAdventureServer
import x.withlithum.neoware.adventure.server.config.AdventureServerSettings
import x.withlithum.neoware.framework.server.ConsoleUtil

fun main() {
    // Start server
    AdventureServerSettings.init()
    val server = NeoAdventureServer(System.getProperty("user.dir").toPath())
    server.start()

    val consoleThread = ConsoleUtil.createConsole(server)
    consoleThread.start()
}