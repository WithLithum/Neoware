/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.behaviour

import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.block.Block
import x.withlithum.neoware.instance.block.statuses.BlockStates
import x.withlithum.neoware.instance.block.statuses.DoubleBlockHalf
import x.withlithum.neoware.level.block.BlockTags

object BlockBehaviours {
    fun useDoor(event: PlayerBlockInteractEvent) {
        val block = event.block
        if (event.player.isSneaking || block == Block.IRON_TRAPDOOR.asKey()) {
            return
        }

        event.isBlockingItemUse = true

        val isOpen = BlockStates.isOpen(block)

        // Determine where the other half is
        val otherPos = when(BlockStates.getDoubleHalf(block)) {
            DoubleBlockHalf.UPPER -> event.blockPosition.add(0, -1, 0)
            DoubleBlockHalf.LOWER -> event.blockPosition.add(0, 1, 0)
        }

        // If the other door block exists, flip that as well
        val otherBlock = event.instance.getBlock(otherPos)
        if (BlockTags.DOORS.contains(otherBlock)) {
            event.instance.setBlock(otherPos, BlockStates.withOpen(otherBlock, !isOpen))
        }

        // Flip the open state of the current block
        event.instance.setBlock(event.blockPosition, BlockStates.withOpen(block, !isOpen))
    }

    fun useTrapdoor(event: PlayerBlockInteractEvent) {
        val block = event.block
        if (event.player.isSneaking || block == Block.IRON_TRAPDOOR.asKey()) {
            return
        }

        event.isBlockingItemUse = true

        val open = BlockStates.isOpen(block)

        event.instance.setBlock(event.blockPosition, BlockStates.withOpen(block, !open))
    }
}