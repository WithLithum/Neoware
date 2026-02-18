/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.locale;

import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.document.TomlDocument;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.io.resources.ResourceHelper;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NullMarked
public final class ResourceLocaleLoader {
    private static final JToml TOML = JToml.jToml();

    private ResourceLocaleLoader() {
        throw new AssertionError("No ResourceLocaleLoader instances for you!");
    }

    public static @Unmodifiable Map<String, Map<String, MessageFormat>> loadFromResources(
        ClassLoader classLoader
    ) {
        try {
            final var resources = ResourceHelper.listDirectory("neoware/lang", classLoader);
            final var map = new HashMap<String, Map<String, MessageFormat>>();
            for (final var resource : resources) {
                final var result = resolveResource(resource.url());
                switch (result) {
                    case NeoResult.Error<Map<String, MessageFormat>> e -> {
                        log.warn("Error occurred when resolving embedded language file '{}'", resource.name());
                        e.logWarn(log);
                    }
                    case NeoResult.Ok<Map<String, MessageFormat>> o -> map.put(resource.name(), o.getValue());
                }
            }

            return map;

        } catch (IOException e) {
            log.warn("Failed to load embedded language files", e);
            return Collections.emptyMap();
        }
    }

    private static NeoResult<@Unmodifiable Map<String, MessageFormat>> resolveResource(URL url) {
        TomlDocument document;
        try (final var reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            document = TOML.read(reader);
        } catch (IOException e) {
            return NeoResult.error("Failed to read resource", e);
        }

        final var hashMap = new HashMap<String, MessageFormat>();
        for (final var key : document.keys(true)) {
            final var value = document.get(key);
            if (value == null || !value.isPrimitive()) {
                log.warn("Invalid value for key '{}'", key);
                continue;
            }

            final var primitive = value.asPrimitive();
            if (!primitive.isString()) {
                log.warn("Value for key '{}' is not a string", key);
                continue;
            }

            final var pattern = primitive.asString();
            try {
                hashMap.put(key.toString(), new MessageFormat(pattern));
            } catch (IllegalArgumentException e) {
                log.warn("Failed to parse pattern '{}'", pattern, e);
            }
        }

        return NeoResult.ok(Collections.unmodifiableMap(hashMap));
    }
}
