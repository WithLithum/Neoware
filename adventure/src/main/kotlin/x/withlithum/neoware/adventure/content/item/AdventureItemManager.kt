/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content.item

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.component.DataComponents
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import x.withlithum.neoware.adventure.content.AdventureContentTree
import x.withlithum.neoware.data.game.ItemRef
import x.withlithum.neoware.util.game.damage
import x.withlithum.neoware.util.results.NeoResult

class AdventureItemManager(private val contentTree: AdventureContentTree) {
    private val cache = HashMap<Key, ItemStack>()

    companion object {
        private val logger = KotlinLogging.logger {}

        private fun missingItemPlaceholder(key: Key): ItemStack {
            return ItemStack.builder(Material.BARRIER)
                .customName(
                    Component.text(
                        "!!! MISSING PROTOTYPE !!!",
                        NamedTextColor.RED, TextDecoration.BOLD, TextDecoration.UNDERLINED
                    )
                )
                .lore(Component.text(key.asString(), NamedTextColor.GRAY))
                .build()
        }
    }

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
        val prototype = contentTree.items[key]
        if (prototype == null) {
            logger.warn { "Unknown item prototype ${key.asString()}" }
            return missingItemPlaceholder(key)
        }

        val result = prototype.createItem(key)
        cache[key] = result
        return result
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
            return NeoResult.ok(ItemStack.AIR)
        }

        var item = getItem(ref.base)
        if (ref.damage != 0) {
            item = item.damage(ref.damage)
        }
        if (ref.count > 1) {
            item = item.withAmount(ref.count)
        }

        return NeoResult.ok(item)
    }

    /**
     * Creates a new instance of [ItemRef] from the specified [ItemStack].
     */
    fun createRef(item: ItemStack): NeoResult<ItemRef> {
        if (item.isAir) {
            return NeoResult.ok(ItemRef.AIR)
        }

        val baseTag = item.get(DataComponents.CUSTOM_DATA)?.nbt?.get("base")
        if (baseTag == null || baseTag !is StringBinaryTag || !Key.parseable(baseTag.value())) {
            return NeoResult.error("Missing or invalid base ID")
        }

        val baseKey = Key.key(baseTag.value())
        if (!contentTree.items.containsKey(baseKey)) {
            return NeoResult.error("Unrecognised prototype ID '${baseKey.value()}'")
        }

        return NeoResult.ok(
            ItemRef(
                baseKey,
                item.amount(),
                item.damage
            )
        )
    }
}