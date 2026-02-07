/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io

import net.kyori.adventure.key.Key
import x.withlithum.neoware.data.content.ContentLoader

interface ContentTraverser {
    fun <V> traverse(namespace: String,
                     loader: ContentLoader<V>,
                     storeInto: MutableMap<Key, V>)
}