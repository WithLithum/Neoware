/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

import net.minestom.server.entity.Player
import x.withlithum.neoware.util.persistence.Savable

/**
 * Defines a service that provides saving and loading of player state.
 */
interface PlayerRecorder : Savable {
    /**
     * Stores the player state. Note that implementations may not always immediately store the
     * saved player info.
     */
    fun capturePlayer(player: Player)

    /**
     * Preloads the data for the player from an external source (for example, on disk), if
     * necessary, in a blocking manner.
     *
     * This method should be called at [net.minestom.server.event.player.AsyncPlayerConfigurationEvent],
     * but otherwise must be called before [rewindPlayer] is called.
     */
    fun preRewindPlayer(player: Player)

    /**
     * Restores the state of a player from the last known state captured via [capturePlayer].
     *
     * Before the invocation of this method, [preRewindPlayer] must be called at least once.
     */
    fun rewindPlayer(player: Player)
}