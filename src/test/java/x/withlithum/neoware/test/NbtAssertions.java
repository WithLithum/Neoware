/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.test;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import org.junit.jupiter.api.Assertions;

public final class NbtAssertions {
    private NbtAssertions() {}

    public static void assertFloat(float expected, BinaryTag tag) {
        final var f = Assertions.assertInstanceOf(FloatBinaryTag.class, tag);
        Assertions.assertEquals(expected, f.value());
    }
}
