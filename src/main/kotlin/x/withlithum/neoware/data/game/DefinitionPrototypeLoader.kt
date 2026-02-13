/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.game

import x.withlithum.neoware.data.content.ContentLoader
import x.withlithum.neoware.game.item.DefinitionPrototype
import x.withlithum.neoware.game.item.ItemPrototype
import java.io.InputStream

@Deprecated("Use DefinitionPrototypeDecoder instead.")
class DefinitionPrototypeLoader(val baseLoader: ContentLoader<ItemDefinition>) : ContentLoader<ItemPrototype> {
    override val acceptsExtension: String?
        get() = baseLoader.acceptsExtension

    override fun load(stream: InputStream): Result<ItemPrototype> {
        return baseLoader.load(stream).map {
            DefinitionPrototype(it)
        }
    }
}