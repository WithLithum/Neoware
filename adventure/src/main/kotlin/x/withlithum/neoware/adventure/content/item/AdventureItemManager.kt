/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.StringBinaryTag
import net.minestom.server.component.DataComponents
import net.minestom.server.item.ItemStack
import x.withlithum.neoware.adventure.content.AdventureContentTree
import x.withlithum.neoware.data.game.ItemRef
import x.withlithum.neoware.util.game.damage
import x.withlithum.neoware.util.results.NeoResult

class AdventureItemManager(private val contentTree: AdventureContentTree) {
    val cache = HashMap<Key, ItemStack>()

    /**
     * Gets an item stack for the specified base ID.
     *
     * For this method, `neoware:air` is a special reference key that always results in
     * [ItemStack.AIR] being created, regardless of whether that key exists in the content tree.
     *
     * @param key The base ID.
     * @return
     * The created item, or [ItemStack.AIR] if the base ID is `neoware:air` or was not found.
     */
    fun getItem(key: Key): ItemStack {
        if (key == ItemRef.AIR_KEY) {
            return ItemStack.AIR
        }

        return cache[key] ?: computeItem(key)
    }

    private fun computeItem(key: Key): ItemStack {
        val prototype = contentTree.items[key] ?: return ItemStack.AIR

        return prototype.createItem(key)
    }

    /**
     * Resolves the specified [ItemRef] into a new [ItemStack]. Returns a failure result only if
     * the base ID cannot be resolved.
     *
     * @param ref The reference to resolve.
     * @return The result of the resolution.
     */
    fun resolveRef(ref: ItemRef): NeoResult<ItemStack> {
        if (ref.isAir) {
            return NeoResult.Ok(ItemStack.AIR)
        }

        var item = getItem(ref.base) ?: return NeoResult.Error("Unknown base ID '${ref.base.asString()}'")
        if (ref.damage != 0) {
            item = item.damage(ref.damage)
        }
        if (ref.count > 1) {
            item = item.withAmount(ref.count)
        }

        return NeoResult.Ok(item)
    }

    /**
     * Creates a new instance of [ItemRef] from the specified [ItemStack].
     */
    fun createRef(item: ItemStack): NeoResult<ItemRef> {
        if (item.isAir) {
            return NeoResult.Ok(ItemRef.AIR)
        }

        val baseTag = item.get(DataComponents.CUSTOM_DATA)?.nbt?.get("base")
        if (baseTag == null || baseTag !is StringBinaryTag || !Key.parseable(baseTag.value())) {
            return NeoResult.Error("Missing or invalid base ID")
        }

        val baseKey = Key.key(baseTag.value())
        if (!contentTree.items.containsKey(baseKey)) {
            return NeoResult.Error("Unrecognised prototype ID '${baseKey.value()}'")
        }

        return NeoResult.Ok(ItemRef(baseKey,
            item.amount(),
            item.damage))
    }
}