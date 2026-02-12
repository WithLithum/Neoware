/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
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
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.player.PlayerInfo;
import x.withlithum.neoware.data.storage.PlayerRecorder;
import x.withlithum.neoware.instance.ManagedInstance;
import x.withlithum.neoware.server.security.BanManager;
import x.withlithum.neoware.util.messages.BanMessage;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@NullMarked
public final class PlayerManagerImpl implements PlayerManager {
    private final BanManager banManager;
    private final ManagedInstance instance;
    private final PlayerRecorder recorder;

    private final Path playersDir;
    private final ConcurrentMap<UUID, PlayerInfo> infoCache = new ConcurrentHashMap<>();

    public PlayerManagerImpl(BanManager banManager, ManagedInstance instance, PlayerRecorder recorder, Path playersDir) {
        this.banManager = banManager;
        this.instance = instance;
        this.recorder = recorder;

        log.info("Store player data into: {}", playersDir);
        this.playersDir = playersDir;

        try {
            if (!Files.isDirectory(playersDir)) {
                Files.createDirectory(playersDir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPlayerFile(UUID uuid) {
        return playersDir.resolve(String.format("%s.dat", uuid));
    }

    private @Nullable PlayerInfo getPlayerInfo(UUID uuid) {
        return infoCache.computeIfAbsent(uuid, _ -> {
            var path = getPlayerFile(uuid);
            if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
                return null;
            }

            Result<PlayerInfo> decodeResult;
            try {
                decodeResult = PlayerInfo.CODEC.decode(Transcoder.NBT, BinaryTagIO.reader().read(path));
            } catch (IOException e) {
                log.warn("Failed to load player info for player with UUID {}", path);
                log.warn("details: ", e);
                return null;
            }

            switch (decodeResult) {
                case Result.Ok<PlayerInfo>(PlayerInfo v) -> {
                    return v;
                }
                case Result.Error<PlayerInfo>(String message) -> {
                    log.warn("Failed to decode player info for player with UUID {}: {}", uuid, message);
                    return null;
                }
            }
        });
    }

    @Override
    public void filterLogin(PlayerConnection connection,
                            GameProfile profile) {
        if (banManager.isBanned(profile.uuid())) {
            final var info = banManager.getInfo(profile.uuid());
            assert info != null;
            try {
                connection.kick(BanMessage.INSTANCE.create(info));
            } catch (RuntimeException e) {
                log.warn("Kicking player {} ({}) with fallback parameters", profile.name(), profile.uuid());
                log.warn("Caused by error: ", e);
                connection.kick(Component.text("Banned"));
            }
        }
    }

    @Override
    public void configure(AsyncPlayerConfigurationEvent config) {
        var player = config.getPlayer();
        var data = getPlayerInfo(player.getUuid());
        config.setSpawningInstance(instance.getInstance());

        if (data == null) {
            player.setRespawnPoint(new Pos(-251, -17, 142));
            return;
        }

        // Preload player data from recorder
        recorder.preRewindPlayer(player);

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
