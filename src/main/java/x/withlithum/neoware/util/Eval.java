/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class Eval {
    private Eval() {
        throw new AssertionError("No Eval instances for you!");
    }

    public static String eitherText(@Nullable String a, String b) {
        if (a == null || a.isBlank()) {
            return b;
        } else {
            return a;
        }
    }

    /**
     * Returns the second argument if the first argument is {@code null}.
     * @param a The first argument.
     * @param b The second argument.
     * @return The first argument, if it is not null; otherwise, the second argument.
     * @param <V> The type of arguments.
     */
    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static <V> @Nullable V either(@Nullable V a, @Nullable V b) {
        if (a != null) {
            return a;
        } else return b;
    }
}
