/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.server;

import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventNode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.adventure.content.AdventureContentTree;
import x.withlithum.neoware.adventure.content.AdventureContentTreeFactory;
import x.withlithum.neoware.adventure.content.item.AdventureItemManager;
import x.withlithum.neoware.adventure.level.LobbyInstance;
import x.withlithum.neoware.adventure.player.AdventurePlayerRecorder;
import x.withlithum.neoware.adventure.security.AdventureWorldSecurity;
import x.withlithum.neoware.data.content.packs.ContentPackLoader;
import x.withlithum.neoware.level.instances.InstanceCapsule;
import x.withlithum.neoware.server.ServerSkeleton;
import x.withlithum.neoware.server.config.ServerSettings;
import x.withlithum.neoware.server.player.PlayerManager;
import x.withlithum.neoware.server.player.PlayerManagerImpl;
import x.withlithum.neoware.server.player.PlayerRecorder;

import java.nio.file.Path;

@NullMarked
public class AdventureServer extends ServerSkeleton {
    private @Nullable PlayerRecorder recorder;
    private @Nullable AdventureItemManager itemManager;
    private @Nullable PlayerManager playerManager;
    private @Nullable AdventureContentTree contentTree;
    private @Nullable LobbyInstance instance;

    public AdventureServer(Path basePath) {
        super(ServerSettings.getData().getServer(), basePath);
    }

    @Override
    protected void bootstrap() {
        // Content tree & core dependencies
        contentTree = ContentPackLoader.loadAllMerged(basePath.resolve("content"),
            AdventureContentTreeFactory.INSTANCE);
        itemManager = new AdventureItemManager(contentTree);
        recorder = new AdventurePlayerRecorder(itemManager, basePath.resolve("players"));
        instance = new LobbyInstance();
        playerManager = new PlayerManagerImpl(playerBlocklist(),
            instance,
            recorder);

        // PVP
        MinestomPvP.init();
        final var modernVanilla = CombatFeatures.modernVanilla();

        // Events registration
        final var node = EventNode.all("Neo Adventure Server");
        node.addChild(modernVanilla.createNode());

        MinecraftServer.getGlobalEventHandler().addChild(node);
        MinecraftServer.getGlobalEventHandler().addChild(AdventureWorldSecurity.INSTANCE.createNode());
    }

    public AdventureItemManager itemManager() {
        if (itemManager == null) {
            throw new IllegalStateException("Attempting to access uninitialized adventure server.");
        }

        return itemManager;
    }

    public InstanceCapsule instance() {
        if (instance == null) {
            throw new IllegalStateException("Attempting to access uninitialized adventure server.");
        }

        return instance;
    }

    public AdventureContentTree contentTree() {
        if (contentTree == null) {
            throw new IllegalStateException("Attempting to access uninitialized adventure server.");
        }

        return contentTree;
    }

    @Override
    public PlayerRecorder playerRecorder() {
        if (recorder == null) {
            throw new IllegalStateException("Attempting to access uninitialized adventure server.");
        }

        return recorder;
    }

    @Override
    public PlayerManager playerManager() {
        if (playerManager == null) {
            throw new IllegalStateException("Attempting to access uninitialized adventure server.");
        }

        return playerManager;
    }
}
