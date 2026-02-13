/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.security

import x.withlithum.neoware.data.storage.Loadable
import x.withlithum.neoware.data.storage.Savable
import java.util.UUID
import kotlin.time.Instant

interface BanManager : Savable, Loadable {
    @Deprecated("Check if lookup() does not return null instead.",
        ReplaceWith("lookup(uuid) != null"))
    fun isBanned(uuid: UUID): Boolean {
        return lookup(uuid) != null
    }

    @Deprecated("Use lookup() instead.",
        ReplaceWith("this.lookup(uuid)"))
    fun getInfo(uuid: UUID): BanInfo? {
        return lookup(uuid)
    }

    fun lookup(uuid: UUID): BanInfo?

    fun ban(uuid: UUID, reason: String?, until: Instant?): BanInfo

    /**
     * Removes the specified player from the ban list. This command will remove the specified
     * player if they are on the ban list, regardless of whether the ban were expired or not.
     *
     * @param uuid The UUID to pardon.
     * @throws IllegalArgumentException The player is not on the ban list.
     */
    fun remove(uuid: UUID)
}