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
import net.minestom.server.item.Material

/**
 * Implements serialization for [Material]. This implementation may require the server to be
 * bootstrapped first.
 */
object MaterialSerializer : KSerializer<Material> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.neoware.material", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Material
    ) {
        encoder.encodeString(value.key().asString())
    }

    override fun deserialize(decoder: Decoder): Material {
        val string = decoder.decodeString()
        serialCheckKeyParsable(string)

        return Material.fromKey(string) ?:
            throw SerializationException("Unknown item type '$string'. Did you bootstrap?")
    }
}