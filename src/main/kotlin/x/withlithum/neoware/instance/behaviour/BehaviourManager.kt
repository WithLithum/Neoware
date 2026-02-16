/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.behaviour

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.event.trait.BlockEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.registry.RegistryTag

@Deprecated("Use Java BehaviourManager instead")
object BehaviourManager {
    private val blockMap = Int2ObjectOpenHashMap<BlockBehaviour?>()

    init {
        blockMap.defaultReturnValue(null)
    }

    fun createEventNode(): EventNode<BlockEvent> {
        val node = EventNode.type("Block behaviour", EventFilter.BLOCK)
        node.addListener(PlayerBlockInteractEvent::class.java) {
            if (hasBehaviour(it.block)) {
                executeBehaviour(it.block, it)
            }
        }

        return node
    }

    fun addBehaviour(block: Block, behaviour: BlockBehaviour) {
        val id = block.id()
        if (blockMap.containsKey(id)) {
            throw IllegalArgumentException("Block with id $id already exists")
        }

        blockMap[block.id()] = behaviour
    }

    fun addBehaviour(blockTag: RegistryTag<Block>, behaviour: BlockBehaviour) {
        val registry = Block.staticRegistry()
        blockTag.forEach {
            val block = registry.get(it) ?: return@forEach
            addBehaviour(block, behaviour)
        }
    }

    fun hasBehaviour(block: Block) = blockMap.containsKey(block.id())

    fun executeBehaviour(block: Block, event: PlayerBlockInteractEvent) {
        blockMap[block.id()]?.let {
            val result = it.apply(event.block,
                event.blockPosition,
                event.blockFace,
                event.instance,
                event.hand,
                event.player)

            when (result) {
                BlockUseAction.CANCEL -> event.isCancelled = true
                BlockUseAction.USE_BLOCK -> {
                    event.isBlockingItemUse = true
                }
                BlockUseAction.CONSUME -> {
                    event.player.getItemInHand(event.hand).consume(1)
                }
                BlockUseAction.NONE -> {}
            }
        }
    }
}