/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import x.withlithum.neoware.main.NeoWareConsole;

public final class ServerConsole {
    private ServerConsole() {
        throw new AssertionError("No ServerConsole instances for you!");
    }

    public static Thread create(NeoServer server) {
        // Console thread setup
        final var console = new NeoWareConsole(server);
        final var consoleThread = new Thread(console::start);
        consoleThread.setDaemon(true);
        consoleThread.setName("Console thread");

        return consoleThread;
    }
}
