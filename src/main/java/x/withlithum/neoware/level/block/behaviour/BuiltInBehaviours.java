/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.behaviour;

import net.minestom.server.instance.block.Block;
import x.withlithum.neoware.instance.block.statuses.BlockStates;
import x.withlithum.neoware.instance.block.statuses.DoubleBlockHalf;
import x.withlithum.neoware.level.block.BlockTags;

public final class BuiltInBehaviours {
    private BuiltInBehaviours() {
        throw new AssertionError("No BuiltInBehaviours instances for you!");
    }

    public static void addBehaviours(BehaviourManager manager) {
        manager.addAll(BlockTags.DOORS, BuiltInBehaviours::onDoorUsed);
        manager.addAll(BlockTags.TRAPDOORS, BuiltInBehaviours::onTrapdoorUsed);
    }

    private static BlockInteractAction onDoorUsed(BlockInteractionInfo interact) {
        final var player = interact.player();
        final var block = interact.block();
        final var blockKey = block.asKey();

        if (player.isSneaking() || (blockKey != null && blockKey.equals(Block.IRON_TRAPDOOR.asKey()))) {
            return BlockInteractAction.NONE;
        }

        final var otherPos = switch (BlockStates.INSTANCE.getDoubleHalf(block)) {
            case DoubleBlockHalf.UPPER -> interact.pos().add(0, -1, 0);
            case DoubleBlockHalf.LOWER -> interact.pos().add(0, 1, 0);
        };

        final var otherBlock = interact.level().getBlock(otherPos);
        if (BlockTags.DOORS.contains(otherBlock)) {
            interact.level().setBlock(otherPos, BlockStates.INSTANCE.withOpen(otherBlock, !BlockStates.INSTANCE.isOpen(otherBlock)));
        }

        interact.level().setBlock(interact.pos(), BlockStates.INSTANCE.withOpen(block, !BlockStates.INSTANCE.isOpen(block)));

        return BlockInteractAction.USE_BLOCK;
    }

    private static BlockInteractAction onTrapdoorUsed(BlockInteractionInfo interact) {
        final var player = interact.player();
        final var block = interact.block();

        if (player.isSneaking()) {
            return BlockInteractAction.NONE;
        }

        final var open = BlockStates.INSTANCE.isOpen(block);

        interact.level().setBlock(interact.pos(), BlockStates.INSTANCE.withOpen(block, !open));
        return BlockInteractAction.USE_BLOCK;
    }
}
