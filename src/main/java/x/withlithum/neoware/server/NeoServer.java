/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.level.instances.InstanceCapsule;
import x.withlithum.neoware.server.player.PlayerBlocklist;
import x.withlithum.neoware.server.player.PlayerManager;
import x.withlithum.neoware.server.player.PlayerRecorder;

@NullMarked
public interface NeoServer {
    PlayerRecorder playerRecorder();
    PlayerManager playerManager();
    PlayerBlocklist playerBlocklist();

    void start();
    void stop();

    boolean isRunning();
}
