/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.minestom.server.command.builder.Command;
import x.withlithum.neoware.server.NeoServer;

public interface CommandBuildable {
    Command build(NeoServer server);
}
