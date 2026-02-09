/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.storage

import net.minestom.server.codec.Codec
import net.minestom.server.codec.StructCodec
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.inventory.PlayerInventory
import net.minestom.server.utils.inventory.PlayerInventoryUtils
import x.withlithum.neoware.data.encode.GameModeOrdinalCodec
import x.withlithum.neoware.data.encode.Vector2FCodec
import x.withlithum.neoware.util.castRotation
import java.util.UUID

data class PlayerRecord(val air: Short,
                        val fire: Short,
                        val pos: Point,
                        val rotation: Vector2F,
                        val onGround: Boolean,
                        val inventory: List<ItemStackWithSlot>,
                        val gameMode: GameMode,
                        val uuid: UUID
    ) {
    companion object {
        val CODEC: Codec<PlayerRecord> = StructCodec.struct(
            "Air", Codec.SHORT, PlayerRecord::air,
            "Fire", Codec.SHORT, PlayerRecord::fire,
            "Pos", Codec.VECTOR3D, PlayerRecord::pos,
            "Rotation", Vector2FCodec, PlayerRecord::rotation,
            "OnGround", Codec.BOOLEAN, PlayerRecord::onGround,
            "Inventory", ItemStackWithSlot.CODEC.list(), PlayerRecord::inventory,
            "playerGameType", GameModeOrdinalCodec, PlayerRecord::gameMode,
            "UUID", Codec.UUID, PlayerRecord::uuid,
            ::PlayerRecord
        )

        fun create(player: Player): PlayerRecord {
            return PlayerRecord(
                air = 300,
                fire = player.fireTicks.toShort(),
                pos = player.position,
                rotation = castRotation(player.position),
                onGround = player.isOnGround,
                inventory = translateInventory(player.inventory),
                gameMode = player.gameMode,
                uuid = player.uuid
            )
        }

        private fun translateInventory(inventory: PlayerInventory): List<ItemStackWithSlot> {
            val stacks = inventory.itemStacks
            val result = ArrayList<ItemStackWithSlot>()

            for (i in 0 until stacks.size) {
                val slot: Int
                if (PlayerInventoryUtils.isPlayerInventorySlot(i)) {
                    slot = PlayerInventoryUtils.convertMinestomSlotToPlayerInventorySlot(i)
                } else {
                    continue
                }

                val entry = ItemStackWithSlot(slot, stacks[i])
                result.add(entry)
            }

            return result
        }

        private fun applyInventory(inventory: PlayerInventory,
                                   data: List<ItemStackWithSlot>) {
            for (stack in data) {
                val slot = PlayerInventoryUtils.convertPlayerInventorySlotToMinestomSlot(stack.slot)
                inventory.setItemStack(slot, stack.itemStack)
            }
        }
    }

    fun apply(player: Player) {
        player.gameMode = gameMode
        player.teleport(Pos(pos,
            rotation.x,
            rotation.y))
        player.fireTicks = fire.toInt()
        player.refreshOnGround(onGround)
        applyInventory(player.inventory, inventory)
    }
}
