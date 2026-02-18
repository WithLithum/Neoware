/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.codec.Codec;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.util.io.FsHelper;
import x.withlithum.neoware.util.results.NeoResult;
import x.withlithum.neoware.util.results.NeoStatus;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@NullMarked
public final class PlayerBlocklistImpl implements PlayerBlocklist {
    private final Path dataFile;
    private Map<UUID, PlayerBlocklistEntry> data = new HashMap<>();

    public PlayerBlocklistImpl(Path dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public @Nullable PlayerBlocklistEntry lookup(UUID uuid) {
        final var info = data.get(uuid);
        if (info == null) {
            return null;
        }

        return info.hasExpired() ? null : info;
    }

    @Override
    public PlayerBlocklistEntry insert(UUID uuid, @Nullable String reason, @Nullable Instant until) {
        final var data = new PlayerBlocklistEntry(reason, until);
        this.data.put(uuid, data);
        return data;
    }

    @Override
    public boolean remove(UUID uuid) {
        return data.remove(uuid) != null;
    }

    @Override
    public void load() {
        final var result = FsHelper.decodeJson(dataFile, Codec.UUID_STRING.mapValue(PlayerBlocklistEntry.CODEC));
        switch (result) {
            case NeoResult.Ok<Map<UUID, PlayerBlocklistEntry>> ok -> this.data = ok.getValue();
            case NeoResult.Error<Map<UUID, PlayerBlocklistEntry>> err -> err.logWarn(log);
        }
    }

    @Override
    public void save() {
        final var status = FsHelper.encodeJson(dataFile, Codec.UUID_STRING.mapValue(PlayerBlocklistEntry.CODEC), data);
        if (status instanceof NeoStatus.Error err) {
            log.warn(err.getMessage(), err.getCause());
        }
    }
}
