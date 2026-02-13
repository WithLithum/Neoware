/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import net.minestom.server.entity.Player;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.persistence.Savable;

@NullMarked
public interface PlayerRecorder extends Savable {
    /**
     * Stores the player state. Note that implementations may not always immediately store the
     * saved player info.
     */
    void capturePlayer(Player player);

    /**
     * Preloads the data for the player from an external source (for example, on disk), if
     * necessary, in a blocking manner.
     * <p>
     * This method <b>must</b> be called before {@link #rewindPlayer} is called for the specified
     * player.
     * </p>
     */
    void preRewindPlayer(Player player);

    /**
     * Restores the state of a player from the last known state captured via [capturePlayer].
     * <p>
     * Before the invocation of this method, {@link #preRewindPlayer} must be called at least once,
     * to allow the implementation to prepare for rewinding.
     */
    void rewindPlayer(Player player);
}
