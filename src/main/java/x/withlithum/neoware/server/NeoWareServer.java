/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import lombok.Getter;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import okio.FileSystem;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.data.content.hierarchy.ContentTree;
import x.withlithum.neoware.data.content.packs.ContentPackLoader;
import x.withlithum.neoware.instance.LobbyInstance;
import x.withlithum.neoware.server.config.Configs;
import x.withlithum.neoware.server.player.PlayerManager;
import x.withlithum.neoware.server.player.PlayerManagerImpl;
import x.withlithum.neoware.server.security.BanManager;
import x.withlithum.neoware.server.security.BanManagerImpl;

import java.nio.file.Path;

public final class NeoWareServer {
    public static final int PACK_DATA_VERSION = 1;

    @Getter
    private boolean isRunning = true;
    private final Path basePath;
	private final MinecraftServer mcServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoWareServer.class);

    public static final NeoWareServer INSTANCE = new NeoWareServer(Path.of(System.getProperty("user.dir")));

    public final PlayerManager playerManager;
    public final BanManager banManager;

    public final LobbyInstance lobby;

    @Nullable
    private ContentTree contents;

	private NeoWareServer(Path basePath) {
        LOGGER.debug("Server instantiated");
        this.basePath = basePath;

		mcServer = MinecraftServer.init(new Auth.Online());
        lobby = new LobbyInstance();
        playerManager = new PlayerManagerImpl(basePath.resolve("players"));
        banManager = new BanManagerImpl(basePath.resolve("ban.json"));
	}
	
	/**
	 * Starts the server.
	 */
	public void start() {
        final var config = Configs.get();
        final var address = config.getString(Configs.KEY_SERVER_ADDRESS);
        final var port = config.getInt(Configs.KEY_SERVER_PORT);

        LOGGER.info("Loading contents");

        contents = ContentPackLoader.INSTANCE.loadAll(okio.Path.get(basePath.resolve("content")),
            FileSystem.SYSTEM);

        LOGGER.info("Starting server");

        Bootstrap.bootstrap();

		mcServer.start(address, port);
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

    public ContentTree getContents() {
        if (contents == null) {
            throw new IllegalStateException("The content tree was not yet loaded.");
        }

        return contents;
    }
}
