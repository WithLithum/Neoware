/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.entity.EquipmentSlotGroup

object EquipmentSlotGroupSerializer : KSerializer<EquipmentSlotGroup> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.neoware.EquipmentSlotGroup", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: EquipmentSlotGroup
    ) {
        encoder.encodeString(value.nbtName())
    }

    override fun deserialize(decoder: Decoder): EquipmentSlotGroup {
        val string = decoder.decodeString()

        try {
            return EquipmentSlotGroup.valueOf(string)
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Invalid equipment slots group string: $string", e)
        }
    }

}