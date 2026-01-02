package x.withlithum.neoware.level.block;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.trait.BlockEvent;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.instance.behaviour.BlockBehaviours;

/**
 * Globally handles certain block interactions.
 */
@Slf4j
@NullMarked
public final class BlockActionAgent {
    /**
     * Creates an event node which the interaction logic will listen from.
     *
     * @return The event node.
     */
    public static EventNode<BlockEvent> createEventNode() {
        var node = EventNode.type("BlockInteractionService", EventFilter.BLOCK);
        node.addListener(PlayerBlockInteractEvent.class, BlockActionAgent::onBlockInteract);

        return node;
    }

    private static void onBlockInteract(PlayerBlockInteractEvent event) {
        var block = event.getBlock();

        if (BlockTags.DOORS.contains(block)) {
            BlockBehaviours.INSTANCE.useDoor(event);
        } else if (BlockTags.TRAPDOORS.contains(block)) {
            BlockBehaviours.INSTANCE.useTrapdoor(event);
        }
    }
}
