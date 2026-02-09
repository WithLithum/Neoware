/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

import net.minestom.server.codec.Codec
import net.minestom.server.codec.StructCodec
import net.minestom.server.item.ItemStack

data class ItemStackWithSlot(val slot: Int,
    val itemStack: ItemStack) {

    companion object {
        val CODEC: Codec<ItemStackWithSlot> = StructCodec.struct(
            "slot", Codec.INT, ItemStackWithSlot::slot,
            StructCodec.INLINE, ItemStack.CODEC, ItemStackWithSlot::itemStack,
            ::ItemStackWithSlot
        )
    }
}
