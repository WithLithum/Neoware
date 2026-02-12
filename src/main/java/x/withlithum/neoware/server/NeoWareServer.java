/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import okio.FileSystem;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.data.content.hierarchy.ContentTree;
import x.withlithum.neoware.data.content.packs.ContentPackLoader;
import x.withlithum.neoware.server.config.ServerSettings;
import x.withlithum.neoware.server.security.BanManager;
import x.withlithum.neoware.server.security.BanManagerImpl;

import javax.naming.OperationNotSupportedException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * @deprecated Inherit from {@link x.withlithum.neoware.framework.server.NeoFrameworkServer} instead.
 */
@Deprecated
public final class NeoWareServer {
    public static final int PACK_DATA_VERSION = 1;

    @Getter
    private boolean isRunning = true;
    private final Path basePath;
	private final MinecraftServer mcServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoWareServer.class);

    public static final NeoWareServer INSTANCE = new NeoWareServer(Path.of(System.getProperty("user.dir")));

//    public final PlayerManager playerManager;
    public final BanManager banManager;

//    public final LobbyInstance lobby;

//    @Nullable
//    private ContentTree contents;

	private NeoWareServer(Path basePath) {
        LOGGER.debug("Server instantiated");
        this.basePath = basePath;

		mcServer = MinecraftServer.init(new Auth.Online());
//        lobby = new LobbyInstance();
        banManager = new BanManagerImpl(basePath.resolve("ban.json"));
//        playerManager = new PlayerManagerImpl(banManager, lobby, basePath.resolve("players"));
	}
	
	/**
	 * Starts the server.
	 */
	public void start() {
        Stopwatch sw  = Stopwatch.createStarted();
        final var serverConfig = ServerSettings.getData().getServer();

//        contents = ContentPackLoader.INSTANCE.loadAll(okio.Path.get(basePath.resolve("content")),
//            FileSystem.SYSTEM);

        Bootstrap.bootstrap();

        sw.stop();
        LOGGER.info("Initialization took {} milliseconds", sw.elapsed(TimeUnit.MILLISECONDS));
        LOGGER.info("Starting server on {}:{}", serverConfig.getAddress(), serverConfig.getPort());
		mcServer.start(serverConfig.getAddress(), serverConfig.getPort());
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
        SaveAll.save();
		if (!MinecraftServer.isStopping())
		{
			MinecraftServer.stopCleanly();
		}
        isRunning = false;
	}

    @Deprecated(forRemoval = true)
    public ContentTree getContents() {
        throw new UnsupportedOperationException("Use your module's loader instead.");
    }
}
