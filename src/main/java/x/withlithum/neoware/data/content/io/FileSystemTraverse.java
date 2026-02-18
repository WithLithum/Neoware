/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.io.PathHelper;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

@NullMarked
@Slf4j
public final class FileSystemTraverse {
    private FileSystemTraverse() {
        throw new AssertionError("No FileSystemTraverse instances for you!");
    }

    @SuppressWarnings("PatternValidation")
    public static <V> void execute(Path directory,
                                   String namespace,
                                   ContentDecoder<V> decoder,
                                   Map<Key, V> storeInto) {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(String.format("'%s' is not a directory", directory));
        }
        if (!Key.parseableNamespace(namespace)) {
            throw new IllegalArgumentException(String.format("namespace '%s' is not a valid namespace", namespace));
        }

        try (final var s = Files.walk(directory)) {
            s.forEach(path -> {
                if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) ||
                    !PathHelper.getExtension(path.toString()).equalsIgnoreCase(decoder.getAcceptedExtension())) {
                    return;
                }

                final var itemPath = ContentIo.keyPathFromFilePath(path, directory);
                if (!Key.parseableValue(itemPath)) {
                    log.warn("Unacceptable item path '{}' in namespace '{}'", itemPath, namespace);
                    return;
                }

                final var key = Key.key(namespace, itemPath);
                NeoResult<V> result;

                try (final var stream = Files.newInputStream(path, StandardOpenOption.READ)) {
                    result = decoder.load(stream);
                } catch (IOException e) {
                    log.warn("Error while reading file '{}' in namespace '{}'", itemPath, namespace, e);
                    return;
                }

                switch (result) {
                    case NeoResult.Ok<V> o -> storeInto.put(key, o.getValue());
                    case NeoResult.Error<V> e -> e.logWarn(log);
                    default -> throw new AssertionError("Unexpected result " + result);
                }
            });
        } catch (IOException io) {
            log.warn("Unable to traverse file system", io);
        }
    }
}
