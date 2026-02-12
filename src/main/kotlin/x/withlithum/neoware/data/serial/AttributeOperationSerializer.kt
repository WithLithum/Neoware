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
import net.minestom.server.entity.attribute.AttributeOperation

object AttributeOperationSerializer : KSerializer<AttributeOperation> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.neoware.AttributeOperation", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: AttributeOperation
    ) {
        encoder.encodeString(when(value) {
            AttributeOperation.ADD_VALUE -> "add_value"
            AttributeOperation.ADD_MULTIPLIED_BASE -> "add_multiplied_base"
            AttributeOperation.ADD_MULTIPLIED_TOTAL -> "add_multiplied_total"
        })
    }

    override fun deserialize(decoder: Decoder): AttributeOperation {
        return when(val string = decoder.decodeString()) {
            "add_value" -> AttributeOperation.ADD_VALUE
            "add_multiplied_base" -> AttributeOperation.ADD_MULTIPLIED_BASE
            "add_multiplied_total" -> AttributeOperation.ADD_MULTIPLIED_TOTAL
            else -> throw SerializationException("Unsupported attribute operation: $string")
        }
    }
}