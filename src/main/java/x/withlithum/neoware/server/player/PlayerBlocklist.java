/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.util.persistence.Loadable;
import x.withlithum.neoware.util.persistence.Savable;

import java.time.Instant;
import java.util.UUID;

/**
 * Defines a service that manages a player blocklist.
 */
@NullMarked
public interface PlayerBlocklist extends Loadable, Savable {
    /**
     * Gets a valid blocklist entry for the specified UUID, or {@code null} if the blocklist entry
     * does not exist for the specified UUID or has expired.
     *
     * @param uuid The UUID to lookup.
     * @return The blocklist entry, or {@code null} if none applicable is found.
     */
    @Nullable PlayerBlocklistEntry lookup(UUID uuid);

    /**
     * Inserts a new entry to the blocklist. If another blocklist entry is associated with the
     * specified UUID, it is replaced.
     * @param uuid The UUID to block.
     * @param reason The reason of the block.
     * @param until The time until the block is lifted, or {@code null} if it will never lift.
     * @return The resulting {@link PlayerBlocklistEntry} instance.
     */
    PlayerBlocklistEntry insert(UUID uuid, @Nullable String reason, @Nullable Instant until);

    /**
     * Removes the blocklist entry associated with the specified UUID. This lifts the ban made
     * against it immediately.
     *
     * @param uuid The UUID to remove.
     * @return Whether the UUID existed on the list.
     */
    boolean remove(UUID uuid);
}
