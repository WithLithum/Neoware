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
import java.util.Locale

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

        when (string) {
            "mainhand" -> return EquipmentSlotGroup.MAIN_HAND
            "offhand" -> return EquipmentSlotGroup.OFF_HAND
            "main_hand", "off_hand" -> throw SerializationException("Unacceptable equipment slot group '$string'")
        }

        try {
            return EquipmentSlotGroup.valueOf(string.uppercase(Locale.ROOT))
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Invalid equipment slots group string: $string", e)
        }
    }

}