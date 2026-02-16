/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.behaviour;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.trait.BlockEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.RegistryTag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BehaviourManager {
    private final Int2ObjectMap<@Nullable BlockBehaviour> behaviours = new Int2ObjectOpenHashMap<>();

    public BehaviourManager() {
        behaviours.defaultReturnValue(null);
    }

    public EventNode<BlockEvent> createEventNode() {
        final var node = EventNode.type("BlockBehaviour", EventFilter.BLOCK);
        node.addListener(PlayerBlockInteractEvent.class, this::executeBehaviour);
        return node;
    }

    public void add(Block block, BlockBehaviour behaviour) {
        if (behaviours.containsKey(block.id())) {
            throw new IllegalArgumentException("Block " + block.name() + " already has a behaviour");
        }

        behaviours.put(block.id(), behaviour);
    }

    public void addAll(RegistryTag<Block> tag, BlockBehaviour behaviour) {
        final var registry = Block.staticRegistry();
        for (var blockKey : tag) {
            final var block = registry.get(blockKey);
            if (block == null) {
                continue;
            }

            add(block, behaviour);
        }
    }

    private void executeBehaviour(PlayerBlockInteractEvent event) {
        final var block = event.getBlock();
        final var behaviour = behaviours.get(block.id());
        if (behaviour == null) {
            return;
        }

        final var info = new BlockInteractionInfo(block,
            event.getBlockPosition(),
            event.getBlockFace(),
            event.getInstance(),
            event.getHand(),
            event.getPlayer());

        final var result = behaviour.apply(info);

        switch (result) {
            case CANCEL -> event.setCancelled(true);
            case USE_BLOCK -> event.setBlockingItemUse(true);
            case CONSUME -> {
                final var player = event.getPlayer();
                final var consumedStack = player.getItemInHand(event.getHand()).consume(1);
                player.setItemInHand(event.getHand(), consumedStack);
            }
        }
    }
}
