package x.withlithum.neoware.server.player;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
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
import x.withlithum.neoware.game.player.PlayerDataUtil;
import x.withlithum.neoware.server.NeoWareServer;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@NullMarked
public final class PlayerManagerImpl implements PlayerManager {
    private final Path playersDir;
    private final ConcurrentMap<UUID, PlayerInfo> infoCache = new ConcurrentHashMap<>();

    public PlayerManagerImpl(Path playersDir) {
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

    private void savePlayerFile(UUID uuid, PlayerInfo data) {
        var path = getPlayerFile(uuid);
        savePlayerFileInternal(uuid, data, path);
    }

    private CompletableFuture<Void> savePlayerFileAsync(UUID uuid, PlayerInfo data) {
        var path = getPlayerFile(uuid);

        return CompletableFuture.runAsync(() -> savePlayerFileInternal(uuid, data, path));
    }

    private void savePlayerFileInternal(UUID uuid, PlayerInfo data, Path path) {
        try (var stream = Files.newOutputStream(path, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {
            var tag = PlayerInfo.CODEC.encode(Transcoder.NBT, data);
            if (tag instanceof Result.Error<BinaryTag>(String message)) {
                log.warn("Failed to encode player info for player with UUID {}: {}", uuid, message);
                return;
            }

            BinaryTagIO.writer().write((CompoundBinaryTag) tag.orElseThrow(), stream);

        } catch (IOException e) {
            log.warn("Could not save player data for player with UUID '{}'", uuid);
            log.warn("details: ", e);
        }

        infoCache.put(uuid, data);
    }

    @Override
    public void filterLogin(PlayerConnection connection,
                            GameProfile profile) {
        // TODO implement ban
    }

    @Override
    public void configure(AsyncPlayerConfigurationEvent config) {
        var player = config.getPlayer();
        var data = getPlayerInfo(player.getUuid());

        if (data == null) {
            log.info("New player: {} ({})", player.getUsername(), player.getUuid());
            config.setSpawningInstance(NeoWareServer.INSTANCE.levelOrchestrator.lobbyLevel);
            player.setRespawnPoint(new Pos(-251, -15, 142));
            return;
        }

        config.setSpawningInstance(NeoWareServer.INSTANCE.levelOrchestrator.getKnownInstance(data.lastInstance()));

        Audiences.players().sendMessage(Component.translatable()
            .key("multiplayer.player.joined")
            .arguments(player.getName())
            .color(NamedTextColor.YELLOW));
    }

    public void saveAll() {
        for (var player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            savePlayerFile(player.getUuid(), PlayerDataUtil.createPlayerInfo(player));
        }
    }

    @Override
    public CompletableFuture<Void> saveAsync(Player player) {
        var data = PlayerDataUtil.createPlayerInfo(player);

        return savePlayerFileAsync(player.getUuid(), data);
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

        var data = getPlayerInfo(player.getUuid());
        if (data != null) {
            PlayerDataUtil.recoverPlayer(player, data, true);
        }
    }

    private void onDisconnect(PlayerDisconnectEvent event) {
        var player = event.getPlayer();
        var playerName = player.getName();
        var playerUuid = player.getUuid();

        Audiences.players().sendMessage(Component.translatable()
            .key("multiplayer.player.left")
            .arguments(playerName)
            .color(NamedTextColor.YELLOW));
        log.info("{} ({}) disconnected", playerName, playerUuid);

        saveAsync(player)
            .whenComplete((_, ex) -> {
                if (ex != null) {
                    log.warn("Unable to save player '{}' ({})", playerName, playerUuid);
                    log.warn("details: ", ex);
                }
            });
    }
}
