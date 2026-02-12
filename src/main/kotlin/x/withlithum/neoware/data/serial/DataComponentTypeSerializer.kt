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
import net.minestom.server.component.DataComponent

object DataComponentTypeSerializer : KSerializer<DataComponent<*>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.DataComponentType", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: DataComponent<*>
    ) {
        encoder.encodeString(value.key().asString())
    }

    override fun deserialize(decoder: Decoder): DataComponent<*> {
        val key = decoder.decodeString()
        serialCheckKeyParsable(key)

        return DataComponent.fromKey(key)
            ?: throw SerializationException("Unknown data component type '$key'")
    }
}