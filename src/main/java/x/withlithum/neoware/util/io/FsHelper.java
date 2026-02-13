/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io;

import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.results.NeoStatus;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@NullMarked
public final class FsHelper {
    private FsHelper() {
        throw new AssertionError("No FsHelper instance for you!");
    }

    /**
     * Executes {@link Files#list(Path)} with the specified function.
     * @param start The path to start walking from.
     * @param function The function to run on every path.
     * @return The execution result state.
     */
    public static NeoStatus forEach(Path start, Consumer<Path> function) {
        try (final var enumerable = Files.list(start)) {
            enumerable.forEach(function);
        } catch (IOException e)  {
            return new NeoStatus.Error("I/O error", e);
        }

        return NeoStatus.Ok.INSTANCE;
    }
}
