/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key

object KeySerializer : KSerializer<Key> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.neoware.key", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Key
    ) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): Key {
        val string = decoder.decodeString()
        serialCheckKeyParsable(string)

        return Key.key(string)
    }
}