package x.withlithum.neoware.level.block;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.trait.BlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jspecify.annotations.NullMarked;

/**
 * Globally handles certain block interactions.
 */
@Slf4j
@NullMarked
public final class BlockActionAgent {
    /**
     * Creates an event node which the interaction logic will listen from.
     * @return The event node.
     */
    public static EventNode<BlockEvent> createEventNode() {
        var node = EventNode.type("BlockInteractionService", EventFilter.BLOCK);
        node.addListener(PlayerBlockInteractEvent.class, BlockActionAgent::onBlockInteract);

        return node;
    }

    private static void onBlockInteract(PlayerBlockInteractEvent event) {

        var block = event.getBlock();
        var pos = event.getBlockPosition();
        var instance = event.getInstance();
        if (BlockTags.DOORS.contains(block)) {
            handleDoor(block, pos, instance);
        }
    }

    private static void handleDoor(Block block, BlockVec pos, Instance instance) {
        var half = block.getProperty("half");
        if (half == null) {
            log.warn("Door block has no half property? at {}, {}, {}", pos.x(), pos.y(), pos.z());
            return;
        }

        var isOpenProp = block.getProperty("open");
        if (isOpenProp == null) {
            log.warn("Door block has no open property? at {}, {}, {}", pos.x(), pos.y(), pos.z());
            return;
        }

        var otherHalfPos = switch(half) {
            case "lower" -> pos.add(0, 1, 0);
            case "upper" -> pos.add(0, -1, 0);
            default -> null;
        };
        if (otherHalfPos == null) {
            log.warn("Invalid half: '{}' at {}, {}, {}", half, pos.x(), pos.y(), pos.z());
            return;
        }
        var otherHalf = instance.getBlock(otherHalfPos);

        var flipped = isOpenProp.equals("true")
            ? "false"
            : "true";

        instance.setBlock(pos, block.withProperty("open", flipped));
        if (BlockTags.DOORS.contains(otherHalf)) {
            instance.setBlock(otherHalfPos, otherHalf.withProperty("open", flipped));
        }
    }
}
