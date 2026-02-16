/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.results.NeoResult;
import x.withlithum.neoware.util.results.NeoStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

@NullMarked
public final class FsHelper {
    private FsHelper() {
        throw new AssertionError("No FsHelper instance for you!");
    }

    private static final Gson GSON = new Gson();

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
            return NeoStatus.error("I/O error", e);
        }

        return NeoStatus.ok();
    }

    public static <V> NeoStatus encodeJson(Path path, Codec<V> codec, V value) {
        final var result = codec.encode(Transcoder.JSON, value);
        if (result instanceof Result.Error<JsonElement>(String message)) {
            return NeoStatus.error(message);
        }

        try (final var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            final var element = result.orElseThrow();
            GSON.toJson(element, writer);
        } catch (IOException e) {
            return NeoStatus.error(e.getLocalizedMessage(), e);
        }

        return NeoStatus.ok();
    }

    public static <V> NeoResult<V> decodeJson(Path path, Codec<V> codec) {
        if (!Files.exists(path)) {
            return NeoResult.error(String.format("File not found: '%s'", path));
        }

        JsonElement element;
        try (final var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            element = JsonParser.parseReader(reader);
        } catch (IOException e) {
            return NeoResult.error(e.getLocalizedMessage(), e);
        }

        return NeoResult.fromMinestom(codec.decode(Transcoder.JSON, element));
    }
}
