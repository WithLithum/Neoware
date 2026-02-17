/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.behaviour;

import net.minestom.server.instance.block.Block;
import x.withlithum.neoware.level.block.BlockTags;
import x.withlithum.neoware.level.block.statuses.BlockProperties;
import x.withlithum.neoware.level.block.statuses.DoubleBlockHalf;

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

        final var otherPos = switch (BlockProperties.getHalf(block)) {
            case DoubleBlockHalf.UPPER -> interact.pos().add(0, -1, 0);
            case DoubleBlockHalf.LOWER -> interact.pos().add(0, 1, 0);
        };

        final var otherBlock = interact.level().getBlock(otherPos);
        final var isOpen = BlockProperties.isOpen(block);
        final var level = interact.level();

        if (BlockTags.DOORS.contains(otherBlock)) {
            level.setBlock(otherPos, BlockProperties.mutateOpen(otherBlock, !isOpen));
        }

        level.setBlock(interact.pos(), BlockProperties.mutateOpen(block, !isOpen));

        return BlockInteractAction.USE_BLOCK;
    }

    private static BlockInteractAction onTrapdoorUsed(BlockInteractionInfo interact) {
        final var player = interact.player();
        final var block = interact.block();

        if (player.isSneaking()) {
            return BlockInteractAction.NONE;
        }

        final var open = BlockProperties.isOpen(block);

        interact.level().setBlock(interact.pos(), BlockProperties.mutateOpen(block, !open));
        return BlockInteractAction.USE_BLOCK;
    }
}
