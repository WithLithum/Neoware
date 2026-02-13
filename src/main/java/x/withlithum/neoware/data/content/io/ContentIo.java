/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Unmodifiable;
import x.withlithum.neoware.util.io.FsHelper;
import x.withlithum.neoware.util.results.NeoResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ContentIo {
    private ContentIo() {
        throw new AssertionError("No ContentIo instances for you!");
    }

    public static <V> NeoResult<@Unmodifiable Map<Key, V>> walkContent(Path basePath,
                                                                       String contentType,
                                                                       ContentDecoder<V> decoder) {
        if (!Files.isDirectory(basePath)) {
            return NeoResult.error("The specified base path is not a directory.", null);
        }

        final var map = new HashMap<Key, V>();

        return FsHelper.forEach(basePath, path -> {
            // Check if it is namespace
            final var name = path.getFileName().toString();
            if (!Files.isDirectory(path) || !Key.parseableNamespace(name)) {
                return;
            }

            // Walk content type dir
            final var contentDir = path.resolve(contentType);
            if (!Files.isDirectory(contentDir)) {
                return;
            }

            // Execute traversal
            FileSystemTraverse.execute(contentDir,
                name,
                decoder,
                map);
        }).withValue(Collections.unmodifiableMap(map));
    }

    public static String keyPathFromFilePath(Path relative, Path root) {
        final var itemPath = root.relativize(relative).toString()
            .replace('\\', '/');

        return itemPath.substring(0, itemPath.lastIndexOf('.'));
    }
}
