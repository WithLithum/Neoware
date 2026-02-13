/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

/**
 * Defines a service which a state can be explicitly loaded from an external storage, such as the
 * file system.
 */
interface Loadable {
    /**
     * Loads the data required by this instance from an external source.
     */
    fun load()
}