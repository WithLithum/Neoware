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
import net.minestom.server.entity.attribute.Attribute

object AttributeSerializer : KSerializer<Attribute> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.attribute", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Attribute
    ) {
        encoder.encodeString(value.key().asString())
    }

    override fun deserialize(decoder: Decoder): Attribute {
        val key = decoder.decodeString()
        serialCheckKeyParsable(key)

        return Attribute.fromKey(key) ?:
            throw SerializationException("Unknown attribute type '$key'")
    }
}