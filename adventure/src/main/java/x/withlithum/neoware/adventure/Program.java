/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure;

import x.withlithum.neoware.adventure.server.AdventureServer;
import x.withlithum.neoware.server.ServerConsole;

import java.nio.file.Path;

final class Program {
    private Program() {
        throw new AssertionError("No Program instances for you!");
    }

    static void main() {
        // Start server
        final var server = new AdventureServer(Path.of(System.getProperty("user.dir")));
        server.start();

        final var consoleThread = ServerConsole.create(server);
        consoleThread.start();
    }
}
