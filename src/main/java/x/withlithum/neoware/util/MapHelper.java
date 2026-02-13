/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Merges two map instances. The second map will overwrite the first map.
 */
@NullMarked
public final class MapHelper {
    private MapHelper() {
        throw new AssertionError("No MapHelper instances for you!");
    }

    public static <K, V> @Unmodifiable Map<K, V> mergeMaps(
        @Unmodifiable Map<K, V> map1,
        @Unmodifiable Map<K, V> map2) {
        final var result = new HashMap<K, V>(map1);
        result.putAll(map2);
        return result;
    }
}
