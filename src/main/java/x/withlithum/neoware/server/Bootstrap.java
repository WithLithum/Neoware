package x.withlithum.neoware.server;

import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.game.item.ItemPrototypes;
import x.withlithum.neoware.level.block.BlockActionAgent;
import x.withlithum.neoware.level.block.NeonBlockHandlers;
import x.withlithum.neoware.level.dimension.NeonDimensionTypes;
import x.withlithum.neoware.level.security.LevelSecurityAgent;
import x.withlithum.neoware.level.worldgen.biome.NeonBiomes;
import x.withlithum.neoware.server.commands.Commands;

public final class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private Bootstrap() {
        throw new AssertionError("No Bootstrap instances for you!");
    }

    public static void bootstrap() {
        LOGGER.info("Bootstrapping NeoWare");
        LOGGER.info("Initializing registries");
        NeonBiomes.initialize();
        NeonDimensionTypes.initialize();
        NeonBlockHandlers.register();
        ItemPrototypes.initialize();

        LOGGER.info("Initializing commands");
        Commands.register();

        LOGGER.info("Initializing combat service");
        MinestomPvP.init();

        var modernVanilla = CombatFeatures.modernVanilla();

        LOGGER.info("Initializing events");
        var eventSource = MinecraftServer.getGlobalEventHandler();
        eventSource.addChild(modernVanilla.createNode());
        eventSource.addChild(NeoWareServer.INSTANCE.playerManager.createEventNode());
        eventSource.addChild(LevelSecurityAgent.createEventNode());
        eventSource.addChild(BlockActionAgent.createEventNode());
    }
}
