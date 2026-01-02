package x.withlithum.neoware.server;

import lombok.Getter;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.level.LevelOrchestrator;
import x.withlithum.neoware.server.player.PlayerManager;
import x.withlithum.neoware.server.player.PlayerManagerImpl;

import java.nio.file.Path;

public final class NeoWareServer {
    @Getter
    private boolean isRunning = true;
	private final MinecraftServer mcServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoWareServer.class);

    public static final NeoWareServer INSTANCE = new NeoWareServer(Path.of(System.getProperty("user.dir")));

    public final LevelOrchestrator levelOrchestrator;
    public final PlayerManager playerManager;

	private NeoWareServer(Path basePath) {
        LOGGER.debug("Server instantiated");
		mcServer = MinecraftServer.init(new Auth.Online());
		levelOrchestrator = new LevelOrchestrator();
        playerManager = new PlayerManagerImpl(basePath.resolve("players"));
	}
	
	/**
	 * Starts the server.
	 */
	public void start() {
        LOGGER.info("Starting server");
        Bootstrap.bootstrap();
		mcServer.start("0.0.0.0", 25565);
	}
	
	/**
	 * Stops the server.
	 * 
	 * @apiNote
	 * Stop commands issued within a tick may not work. Use the scheduler instead if you are trying
	 * to stop from a command.
	 * 
	 * @see MinecraftServer#stopCleanly()
	 */
	public void stop() {
        playerManager.saveAll();
		if (!MinecraftServer.isStopping())
		{
			MinecraftServer.stopCleanly();
		}
        isRunning = false;
	}
}
