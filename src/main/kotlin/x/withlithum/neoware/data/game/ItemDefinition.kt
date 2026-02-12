/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.game

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.component.DataComponent
import net.minestom.server.component.DataComponents
import net.minestom.server.entity.attribute.AttributeModifier
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.component.AttributeList
import net.minestom.server.item.component.CustomData
import net.minestom.server.item.component.EnchantmentList
import net.minestom.server.item.component.TooltipDisplay
import net.minestom.server.item.enchant.Enchantment
import net.minestom.server.registry.RegistryKey
import x.withlithum.neoware.data.serial.DataComponentTypeSerializer
import x.withlithum.neoware.data.serial.KeySerializer
import x.withlithum.neoware.data.serial.MaterialSerializer
import x.withlithum.neoware.data.serial.MiniMessageSerializer
import x.withlithum.neoware.util.ifNotNull

@JvmRecord
@Serializable
data class ItemDefinition(
    @Serializable(with = MaterialSerializer::class)
    val material: Material,
    @Serializable(with = MiniMessageSerializer::class)
    val name: Component? = null,
    val lore: List<@Serializable(MiniMessageSerializer::class) Component>? = null,
    val enchantments: Map<@Serializable(KeySerializer::class) Key, Int>? = null,
    val attributes: List<ItemAttributeRecord>? = null,
    val hideTooltips: Set<@Serializable(DataComponentTypeSerializer::class) DataComponent<*>>? = null
) {
    companion object {
        private fun createEnchantmentList(map: Map<Key, Int>): EnchantmentList {
            val el = HashMap<RegistryKey<Enchantment>, Int>(map.size)
            val registry = MinecraftServer.getEnchantmentRegistry()

            for (entry in map.entries) {
                val regKey = registry.getKey(entry.key) ?: throw IllegalStateException("Unknown enchantment type '${entry.key}'")
                el[regKey] = entry.value
            }

            return EnchantmentList(el)
        }

        private fun createAttributeList(records: List<ItemAttributeRecord>): AttributeList {
            val al = ArrayList<AttributeList.Modifier>(records.size)
            for (entry in records) {
                val display = if (entry.hide) {
                    AttributeList.Display.Hidden.INSTANCE
                } else {
                    AttributeList.Display.Default.INSTANCE
                }

                val item = AttributeList.Modifier(entry.attribute,
                    AttributeModifier(entry.id, entry.amount, entry.operation),
                    entry.slot,
                    display)
                al.add(item)
            }

            return AttributeList(al)
        }

    }

    fun createItem(baseId: Key): ItemStack {
        val builder = ItemStack.builder(material)
        builder.set(DataComponents.CUSTOM_DATA, CustomData(CompoundBinaryTag.builder()
            .put("base", StringBinaryTag.stringBinaryTag(baseId.asString()))
            .build()))
        name.ifNotNull { builder.set(DataComponents.ITEM_NAME, it) }
        lore.ifNotNull { builder.set(DataComponents.LORE, it) }
        enchantments.ifNotNull { builder.set(DataComponents.ENCHANTMENTS, createEnchantmentList(it)) }
        attributes.ifNotNull { builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createAttributeList(it)) }
        hideTooltips.ifNotNull { builder.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(false,
            it)) }
        return builder.build()
    }

}
