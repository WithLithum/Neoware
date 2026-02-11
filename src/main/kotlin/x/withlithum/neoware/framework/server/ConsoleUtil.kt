/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.framework.server

import x.withlithum.neoware.main.NeoWareConsole

object ConsoleUtil {
    fun createConsole(server: NeoFrameworkServer): Thread {
        // Console thread setup
        val console = NeoWareConsole(server)
        val consoleThread = Thread(Runnable { console.start() })
        consoleThread.setDaemon(true)
        consoleThread.setName("Console thread")

        return consoleThread
    }
}