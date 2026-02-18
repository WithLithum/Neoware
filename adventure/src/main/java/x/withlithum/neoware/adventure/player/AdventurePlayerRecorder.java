/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.player;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.adventure.content.item.AdventureItemManager;
import x.withlithum.neoware.adventure.player.data.SavedDataUtils;
import x.withlithum.neoware.data.player.PlayerInfo;
import x.withlithum.neoware.server.player.PlayerRecorder;
import x.withlithum.neoware.util.text.Messages;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@NullMarked
public final class AdventurePlayerRecorder implements PlayerRecorder {
    private final AdventureItemManager itemManager;
    private final Path storagePath;
    private final ConcurrentMap<UUID, PlayerInfo> staging = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventurePlayerRecorder.class.getName());

    public AdventurePlayerRecorder(AdventureItemManager itemManager, Path storagePath) {
        this.itemManager = itemManager;
        this.storagePath = storagePath;
    }

    @Override
    public void capturePlayer(Player player) {
        staging.put(player.getUuid(), SavedDataUtils.recordPlayer(player, itemManager));
    }

    @Override
    public void preRewindPlayer(Player player) {
        final var uuid = player.getUuid();
        if (staging.containsKey(uuid)) {
            return;
        }

        final var data = loadPlayer(uuid);
        if (data != null) {
            staging.put(uuid, data);
        }
    }

    @Override
    public void rewindPlayer(Player player) {
        final var data = staging.get(player.getUuid());
        if (data == null) {
            // Don't rewind because there are nothing to apply
            return;
        }

        if (!SavedDataUtils.applyPlayer(player,
            data,
            itemManager)) {

            Messages.sendError(player, Messages.message("neo_adventure", "join.item_not_fully_restored"));
        }
    }

    @Override
    public void save() {
        if (Files.exists(storagePath, LinkOption.NOFOLLOW_LINKS)
        && !Files.isDirectory(storagePath, LinkOption.NOFOLLOW_LINKS)) {
            throw new IllegalStateException("Invalid storage directory, cannot save.");
        }

        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectory(storagePath);
            } catch (IOException e) {
                LOGGER.error("Failed to create storage directory", e);
                return;
            }
        }

        final var scheduled = new ArrayList<Thread>();
        for (final var pair : staging.entrySet()) {
            scheduled.add(Thread.startVirtualThread(() ->
                savePlayer(pair.getValue(), getFilePath(pair.getKey()))));
        }

        scheduled.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted while waiting for save to complete", e);
            }
        });
    }

    private void savePlayer(PlayerInfo info, Path path) {
        final var encodeResult = PlayerInfo.CODEC.encode(Transcoder.NBT, info);
        if (encodeResult instanceof Result.Error<BinaryTag>(String message)) {
            LOGGER.warn("Failed to encode player info for '{}': {}",
                path.getFileName(),
                message);
            return;
        }

        final var tag = encodeResult.orElseThrow();
        if (!(tag instanceof CompoundBinaryTag compound)) {
            throw new AssertionError("The encode result for player info is not compound!");
        }

        try (final var stream = Files.newOutputStream(path,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {

            BinaryTagIO.writer().write(compound, stream, BinaryTagIO.Compression.GZIP);
        } catch (IOException e) {
            LOGGER.warn("Unable to save player info for '{}'",
                path.getFileName(),
                e);
        }
    }

    private Path getFilePath(UUID uuid) {
        return storagePath.resolve(String.format("%s.dat", uuid));
    }

    private @Nullable PlayerInfo loadPlayer(UUID uuid) {
        final var filePath = getFilePath(uuid);
        if (!Files.isRegularFile(filePath, LinkOption.NOFOLLOW_LINKS)) {
            return null;
        }

        // Read the binary tag file.
        CompoundBinaryTag compound;
        try (final var stream = Files.newInputStream(filePath)) {
            compound = BinaryTagIO.reader().read(stream, BinaryTagIO.Compression.GZIP);
        } catch (IOException e) {
            LOGGER.warn("Failed to load player info from file", e);
            return null;
        }

        // Decode.
        final var decodeResult = PlayerInfo.CODEC.decode(Transcoder.NBT, compound);
        return switch (decodeResult) {
            case Result.Ok<PlayerInfo> o -> o.value();
            case Result.Error<PlayerInfo> e -> {
                LOGGER.warn("Failed to decode player info: {}", e.message());
                yield null;
            }
        };
    }
}
