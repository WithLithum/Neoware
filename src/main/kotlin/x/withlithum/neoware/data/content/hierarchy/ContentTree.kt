/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy

import net.kyori.adventure.key.Key
import x.withlithum.neoware.data.content.ContentLoader
import x.withlithum.neoware.data.content.ContentSource
import x.withlithum.neoware.data.game.DefinitionPrototypeLoader
import x.withlithum.neoware.data.game.ItemDefinition
import x.withlithum.neoware.game.item.ItemPrototype
import x.withlithum.neoware.util.MapHelper

/**
 * An immutable tree of contents.
 */
data class ContentTree(val items: Map<Key, ItemPrototype>) {
    companion object {
        private val ITEM_LOADER =
            DefinitionPrototypeLoader(ContentLoader.toml(ItemDefinition.CODEC))

        val EMPTY = ContentTree(emptyMap())

        fun load(source: ContentSource): ContentTree {
            return ContentTree(
                items = source.loadContents("item", ITEM_LOADER)
            )
        }
    }

    /**
     * Merges this tree with the other content tree. The other tree overrides this tree.
     */
    fun merge(other: ContentTree): ContentTree {
        return ContentTree(MapHelper.mergeMaps(this.items, other.items))
    }
}
