/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content

import net.kyori.adventure.key.Key
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.data.content.hierarchy.ContentTree
import x.withlithum.neoware.data.content.io.ContentIo
import x.withlithum.neoware.data.content.io.DefinitionPrototypeDecoder
import x.withlithum.neoware.data.content.io.GsonCodecContentDecoder
import x.withlithum.neoware.data.game.ItemDefinition
import x.withlithum.neoware.game.item.ItemPrototype

data class AdventureContentTree(val items: Map<Key, ItemPrototype>):
    ContentTree<AdventureContentTree> {
    companion object {
        private val ITEM_LOADER =
            DefinitionPrototypeDecoder(GsonCodecContentDecoder(ItemDefinition.CODEC))

        val EMPTY = AdventureContentTree(emptyMap())

        fun loadDir(dir: Path, fs: FileSystem): AdventureContentTree {
            return AdventureContentTree(
                items = ContentIo.traverse(dir, "item", fs, ITEM_LOADER)
            )
        }
    }

    /**
     * Merges this tree with the other content tree. The other tree overrides this tree.
     */
    override fun merge(other: AdventureContentTree): AdventureContentTree {
        return AdventureContentTree(MapHelper.mergeMaps(this.items, other.items))
    }
}
