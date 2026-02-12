/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

/**
 * Defines a service which a state can be explicitly saved to an external storage, such as the
 * file system.
 */
interface Savable {
    /**
     * Saves the state of this instance to an external storage.
     */
    fun save()
}