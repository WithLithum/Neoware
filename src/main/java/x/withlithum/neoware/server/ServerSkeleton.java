/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.level.block.behaviour.BehaviourManager;
import x.withlithum.neoware.level.block.behaviour.BuiltInBehaviours;
import x.withlithum.neoware.server.commands.Commands;
import x.withlithum.neoware.server.config.ServerListenOptions;
import x.withlithum.neoware.server.player.PlayerBlocklist;
import x.withlithum.neoware.server.player.PlayerBlocklistImpl;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@NullMarked
@Slf4j
public abstract class ServerSkeleton implements NeoServer {
    private final ServerListenOptions listenOptions;
    protected final Path basePath;

    private final MinecraftServer minecraft;
    private final PlayerBlocklist playerBlocklist;
    private final BehaviourManager behaviourManager = new BehaviourManager();
    private boolean isRunning;

    protected ServerSkeleton(ServerListenOptions options,
                             Path basePath) {
        this.basePath = basePath;
        listenOptions = options;
        playerBlocklist = new PlayerBlocklistImpl(basePath.resolve("ban.json"));

        minecraft = MinecraftServer.init(new Auth.Online());
    }

    @Override
    public PlayerBlocklist playerBlocklist() {
        return playerBlocklist;
    }

    protected abstract void bootstrap();

    private void preBootstrap() {
        Bootstrap.bootstrap();
        playerBlocklist.load();
        Commands.register(this);
        BuiltInBehaviours.addBehaviours(behaviourManager);
    }

    private void postBootstrap() {
        final var eventManager = MinecraftServer.getGlobalEventHandler();
        eventManager.addChild(playerManager().createEventNode());
        eventManager.addChild(behaviourManager.createEventNode());
    }

    @Override
    public void start() {
        final var sw = Stopwatch.createStarted();

        preBootstrap();
        bootstrap();
        postBootstrap();

        sw.stop();
        log.info("Server setup took {}ms", sw.elapsed(TimeUnit.MILLISECONDS));

        minecraft.start(listenOptions.getAddress(), listenOptions.getPort());
        isRunning = true;
    }

    @Override
    public void stop() {
        playerBlocklist.save();
        playerRecorder().save();

        if (!MinecraftServer.isStopping()) {
            MinecraftServer.stopCleanly();
        }

        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
