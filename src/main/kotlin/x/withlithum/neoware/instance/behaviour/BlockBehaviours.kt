/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.behaviour

import net.minestom.server.coordinate.BlockVec
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerHand
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import x.withlithum.neoware.instance.block.statuses.BlockStates
import x.withlithum.neoware.instance.block.statuses.DoubleBlockHalf
import x.withlithum.neoware.level.block.BlockTags

object BlockBehaviours {
    fun addDefault() {
        BehaviourManager.addBehaviour(BlockTags.DOORS, this::useDoor)
        BehaviourManager.addBehaviour(BlockTags.TRAPDOORS, this::useTrapdoor)
    }

    fun useDoor(block: Block,
                pos: BlockVec,
                face: BlockFace,
                instance: Instance,
                hand: PlayerHand,
                player: Player): BlockUseAction {
        if (player.isSneaking || block == Block.IRON_TRAPDOOR.asKey()) {
            return BlockUseAction.NONE
        }

        val isOpen = BlockStates.isOpen(block)

        // Determine where the other half is
        val otherPos = when(BlockStates.getDoubleHalf(block)) {
            DoubleBlockHalf.UPPER -> pos.add(0, -1, 0)
            DoubleBlockHalf.LOWER -> pos.add(0, 1, 0)
        }

        // If the other door block exists, flip that as well
        val otherBlock = instance.getBlock(otherPos)
        if (BlockTags.DOORS.contains(otherBlock)) {
            instance.setBlock(otherPos, BlockStates.withOpen(otherBlock, !isOpen))
        }

        // Flip the open state of the current block
        player.instance.setBlock(pos, BlockStates.withOpen(block, !isOpen))

        return BlockUseAction.USE_BLOCK
    }

    fun useTrapdoor(block: Block,
                    pos: BlockVec,
                    face: BlockFace,
                    instance: Instance,
                    hand: PlayerHand,
                    player: Player): BlockUseAction {
        if (player.isSneaking || block == Block.IRON_TRAPDOOR.asKey()) {
            return BlockUseAction.NONE
        }

        val open = BlockStates.isOpen(block)

        instance.setBlock(pos, BlockStates.withOpen(block, !open))
        return BlockUseAction.USE_BLOCK
    }
}