/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.key.Key
import x.withlithum.neoware.data.content.ContentLoader
import x.withlithum.neoware.data.content.ContentSource
import x.withlithum.neoware.data.game.DefinitionPrototypeLoader
import x.withlithum.neoware.data.game.ItemDefinition
import x.withlithum.neoware.game.item.ItemPrototype

class ContentHierarchy(val source: ContentSource) {
    companion object {
        private val LOGGER = KotlinLogging.logger { }
        private val ITEM_LOADER =
            DefinitionPrototypeLoader(ContentLoader.toml(ItemDefinition.CODEC))
    }

    var itemPrototypes: Map<Key, ItemPrototype> = emptyMap()
        private set

    private fun totalSize(): Int {
        return itemPrototypes.size
    }

    fun load() {
        itemPrototypes = source.loadContents("item", ITEM_LOADER)

        LOGGER.info { "Loaded ${totalSize()} items" }
    }
}