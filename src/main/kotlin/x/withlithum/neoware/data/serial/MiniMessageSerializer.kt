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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MiniMessageSerializer : KSerializer<Component> {
    private val miniMessage = MiniMessage.miniMessage()
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("x.withlithum.miniMessage", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Component
    ) {
        encoder.encodeString(miniMessage.serialize(value))
    }

    override fun deserialize(decoder: Decoder): Component {
        val str = decoder.decodeString()
        return miniMessage.deserialize(str)
    }
}