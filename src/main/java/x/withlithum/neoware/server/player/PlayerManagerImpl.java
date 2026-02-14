/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.level.instances.InstanceCapsule;
import x.withlithum.neoware.util.text.BannedMessage;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@NullMarked
public final class PlayerManagerImpl implements PlayerManager {
    private final PlayerBlocklist banManager;
    private final InstanceCapsule instance;
    private final PlayerRecorder recorder;

    public PlayerManagerImpl(PlayerBlocklist banManager,
                             InstanceCapsule instance,
                             PlayerRecorder recorder,
                             Path playersDir) {
        this.banManager = banManager;
        this.instance = instance;
        this.recorder = recorder;

        try {
            if (!Files.isDirectory(playersDir)) {
                Files.createDirectory(playersDir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void filterLogin(PlayerConnection connection,
                            GameProfile profile) {
        final var blockInfo = banManager.lookup(profile.uuid());
        if (blockInfo == null) {
            return;
        }

        try {
            connection.kick(BannedMessage.create(blockInfo));
        } catch (RuntimeException e) {
            log.warn("Kicking player {} ({}) with fallback parameters", profile.name(), profile.uuid());
            log.warn("Caused by error: ", e);
            connection.kick(Component.translatable("multiplayer.disconnect.banned"));
        }
    }

    @Override
    public void configure(AsyncPlayerConfigurationEvent config) {
        var player = config.getPlayer();
        // Preload player data from recorder.
        recorder.preRewindPlayer(player);

        config.setSpawningInstance(instance.instance());
        player.setRespawnPoint(new Pos(-251, -17, 142));

        log.info("Player {} joined with UUID {}", player.getUsername(), player.getUuid());

        Audiences.players().sendMessage(Component.translatable()
            .key("multiplayer.player.joined")
            .arguments(player.getName())
            .color(NamedTextColor.YELLOW));
    }

    @Override
    public EventNode<Event> createEventNode() {
        var parentNode = EventNode.all("PlayerManager");
        parentNode.addListener(AsyncPlayerPreLoginEvent.class,
            event -> filterLogin(event.getConnection(),
                event.getGameProfile()));
        parentNode.addListener(AsyncPlayerConfigurationEvent.class,
            this::configure);
        parentNode.addListener(PlayerDisconnectEvent.class, this::onDisconnect);
        parentNode.addListener(PlayerSpawnEvent.class, this::onSpawn);

        return parentNode;
    }

    private void onSpawn(PlayerSpawnEvent playerSpawnEvent) {
        if (!playerSpawnEvent.isFirstSpawn()) {
            return;
        }

        var player = playerSpawnEvent.getPlayer();
        recorder.rewindPlayer(player);
    }

    private void onDisconnect(PlayerDisconnectEvent event) {
        var player = event.getPlayer();
        var playerName = Component.text(player.getUsername());
        var playerUuid = player.getUuid();

        Audiences.players().sendMessage(Component.translatable()
            .key("multiplayer.player.left")
            .arguments(playerName)
            .color(NamedTextColor.YELLOW));
        log.info("{} ({}) disconnected", playerName, playerUuid);

        recorder.capturePlayer(player);
    }
}
