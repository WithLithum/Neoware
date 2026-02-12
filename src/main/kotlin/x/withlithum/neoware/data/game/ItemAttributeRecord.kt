/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.game

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.minestom.server.entity.EquipmentSlotGroup
import net.minestom.server.entity.attribute.Attribute
import net.minestom.server.entity.attribute.AttributeOperation
import x.withlithum.neoware.data.serial.AttributeOperationSerializer
import x.withlithum.neoware.data.serial.AttributeSerializer
import x.withlithum.neoware.data.serial.EquipmentSlotGroupSerializer
import x.withlithum.neoware.data.serial.KeySerializer

@Serializable
data class ItemAttributeRecord(
    @Serializable(with = KeySerializer::class)
    val id: Key,
    @Serializable(with = AttributeSerializer::class) val attribute: Attribute,
    val amount: Double,
    @Serializable(with = AttributeOperationSerializer::class)
    val operation: AttributeOperation = AttributeOperation.ADD_VALUE,
    @Serializable(with = EquipmentSlotGroupSerializer::class)
    val slot: EquipmentSlotGroup = EquipmentSlotGroup.ANY,
    val hide: Boolean = false
)
