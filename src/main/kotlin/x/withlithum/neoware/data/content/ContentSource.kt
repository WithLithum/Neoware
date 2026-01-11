/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import net.kyori.adventure.key.Key

/**
 * Provides contents from a specified source.
 */
sealed interface ContentSource {
    fun <V> loadContents(rootName: String, loader: ContentLoader<V>): Map<Key, V>
}