/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.main

import x.withlithum.neoware.adventure.server.AdventureServer
import x.withlithum.neoware.server.ServerConsole
import java.nio.file.Path

fun main() {
    // Start server
    val server = AdventureServer(Path.of(System.getProperty("user.dir")))
    server.start()

    val consoleThread = ServerConsole.create(server)
    consoleThread.start()
}