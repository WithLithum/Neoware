/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.behaviour

import net.minestom.server.instance.block.Block
import x.withlithum.neoware.instance.block.statuses.BlockStates
import x.withlithum.neoware.instance.block.statuses.DoubleBlockHalf
import x.withlithum.neoware.level.block.BlockTags
import x.withlithum.neoware.level.block.behaviour.BlockInteractionInfo

object BlockBehaviours {
    fun addDefault(manager: x.withlithum.neoware.level.block.behaviour.BehaviourManager) {
        manager.addAll(BlockTags.DOORS, this::useDoor)
        manager.addAll(BlockTags.TRAPDOORS, this::useTrapdoor)
    }

    fun useDoor(interact: BlockInteractionInfo): BlockUseAction {
        if (interact.player.isSneaking || interact.block == Block.IRON_TRAPDOOR.asKey()) {
            return BlockUseAction.NONE
        }

        val isOpen = BlockStates.isOpen(interact.block)

        // Determine where the other half is
        val otherPos = when(BlockStates.getDoubleHalf(interact.block)) {
            DoubleBlockHalf.UPPER -> interact.pos.add(0, -1, 0)
            DoubleBlockHalf.LOWER -> interact.pos.add(0, 1, 0)
        }

        // If the other door block exists, flip that as well
        val otherBlock = interact.level.getBlock(otherPos)
        if (BlockTags.DOORS.contains(otherBlock)) {
            interact.level.setBlock(otherPos, BlockStates.withOpen(otherBlock, !isOpen))
        }

        // Flip the open state of the current block
        interact.player.instance.setBlock(interact.pos, BlockStates.withOpen(interact.block, !isOpen))

        return BlockUseAction.USE_BLOCK
    }

    fun useTrapdoor(interact: BlockInteractionInfo): BlockUseAction {
        val player = interact.player
        val block = interact.block

        if (player.isSneaking || block == Block.IRON_TRAPDOOR.asKey()) {
            return BlockUseAction.NONE
        }

        val open = BlockStates.isOpen(block)

        interact.level.setBlock(interact.pos, BlockStates.withOpen(block, !open))
        return BlockUseAction.USE_BLOCK
    }
}