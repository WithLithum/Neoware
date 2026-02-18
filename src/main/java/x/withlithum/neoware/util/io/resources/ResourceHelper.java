/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io.resources;

import org.jetbrains.annotations.Unmodifiable;
import x.withlithum.neoware.util.ResourceRef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ResourceHelper {
    private ResourceHelper() {
        throw new AssertionError("No ResourceHelper instances for you!");
    }

    public static @Unmodifiable List<ResourceRef> listDirectory(String path,
                                                                ClassLoader classLoader)
        throws IOException {
        try (final var stream = classLoader.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }

            final var result = new ArrayList<ResourceRef>();
            final var reader = new BufferedReader(new InputStreamReader(stream));

            while (true) {
                final var line = reader.readLine();
                if (line == null) {
                    break;
                }

                final var url = classLoader.getResource(line);
                if (url == null) {
                    continue;
                }

                result.add(new ResourceRef(line, url));
            }

            return result;
        }
    }
}
