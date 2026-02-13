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
import x.withlithum.neoware.util.MapHelper
import x.withlithum.neoware.util.results.NeoResult

data class AdventureContentTree(val items: Map<Key, ItemPrototype>):
    ContentTree<AdventureContentTree> {
    companion object {
        private val ITEM_LOADER =
            DefinitionPrototypeDecoder(GsonCodecContentDecoder(ItemDefinition.CODEC))

        val EMPTY = AdventureContentTree(emptyMap())

        fun loadDir(dir: Path, fs: FileSystem): NeoResult<AdventureContentTree> {
            return when (val result = ContentIo.walkContent(dir.toNioPath(), "item", ITEM_LOADER)) {
                is NeoResult.Ok -> NeoResult.ok(AdventureContentTree(
                    items = result.value
                ))
                is NeoResult.Error -> result.cast()
            }
        }
    }

    /**
     * Merges this tree with the other content tree. The other tree overrides this tree.
     */
    override fun merge(other: AdventureContentTree): AdventureContentTree {
        return AdventureContentTree(MapHelper.mergeMaps(this.items, other.items))
    }
}
