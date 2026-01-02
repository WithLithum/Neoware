package x.withlithum.neoware.level.block;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.BlockEntityType;
import x.withlithum.neoware.level.block.impl.SignHandler;

public final class NeonBlockHandlers {
    private NeonBlockHandlers() {
        throw new AssertionError("No NeonBlockHandlers instances for you!");
    }

    public static void register() {
        var blockManager = MinecraftServer.getBlockManager();

        blockManager.registerHandler(BlockEntityType.HANGING_SIGN.key(), SignHandler::new);
        blockManager.registerHandler(BlockEntityType.SIGN.key(), SignHandler::new);
    }
}
