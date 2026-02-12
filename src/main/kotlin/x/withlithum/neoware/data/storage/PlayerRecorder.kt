/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

import net.minestom.server.entity.Player

interface PlayerRecorder : Savable {
    fun capturePlayer(player: Player)

    /**
     * This method is called at configuration.
     */
    fun preRewindPlayer(player: Player)
    fun rewindPlayer(player: Player)
}