/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.content.hierarchy.ContentTree;
import x.withlithum.neoware.data.content.hierarchy.ContentTreeFactory;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@NullMarked
@Slf4j
public final class ContentPackLoader {
    private ContentPackLoader() {
        throw new AssertionError("No ContentPackLoader instances for you!");
    }

    public static final String PACK_META_FILE = "neoware_pack.json";
    public static final String PACK_DATA_DIR = "data";

    public static <V extends ContentTree<V>> V loadAllMerged(Path path,
                                                             ContentTreeFactory<V> factory) {
        if (!Files.isDirectory(path)) {
            log.warn("'{}' is not a directory", path);
            return factory.getEmpty();
        }

        var loadCount = 0;
        V tree = factory.getEmpty();
        try {
            try (final var s = Files.list(path)
                .filter(Files::isDirectory)) {
                final var list = s.toList();

                for (var f : list) {
                    if (!Files.isDirectory(f)) {
                        continue;
                    }

                    final var pack = loadOne(f, factory);
                    switch (pack) {
                        case NeoResult.Error<V> e -> e.logWarn(log);
                        case NeoResult.Ok<V> o -> {
                            tree = tree.merge(o.getValue());
                            loadCount++;
                        }
                        default -> throw new IllegalStateException("Impossible state");
                    }
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            log.warn("Failed to load content tree", e);
        }

        log.info("Loaded {} content packs", loadCount);
        return tree;
    }

    public static <V extends ContentTree<V>> NeoResult<V> loadOne(Path path,
                                                                  ContentTreeFactory<V> factory) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        final var packMeta = loadPackMeta(path);
        if (packMeta == null) {
            return new NeoResult.Error<>("Failed to load pack meta", null);
        }
        if (packMeta.dataVersion() != factory.getDataVersion()) {
            return new NeoResult.Error<>("Unsupported pack version", null);
        }

        final var dataPath = path.resolve(PACK_DATA_DIR);
        if (!Files.isDirectory(dataPath)) {
            return new NeoResult.Error<>("Data path is not a directory or does not exist", null);
        }

        return factory.loadDir(path);
    }

    private static @Nullable ContentPackMeta loadPackMeta(Path packBase) {
        final var metaFile = packBase.resolve(PACK_META_FILE);
        if (!Files.isRegularFile(metaFile)) {
            log.warn("Cannot find meta file '{}', metaFile", metaFile);
            return null;
        }

        try (final var reader = Files.newBufferedReader(metaFile)) {
            final var result = ContentPackMeta.CODEC.decode(Transcoder.JSON,
                JsonParser.parseReader(reader));
            switch (result) {
                case Result.Ok<ContentPackMeta> ok -> { return ok.value(); }
                case Result.Error<ContentPackMeta> err -> {
                    log.warn("Failed to decode meta file '{}': {}", metaFile, err.message());
                    return null;
                }
            }

        } catch (JsonSyntaxException | IOException e) {
            log.warn("Failed to read meta file", e);
            return null;
        }
    }
}
