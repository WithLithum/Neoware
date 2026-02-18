/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode;

import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.minestom.server.codec.Transcoder;
import org.junit.jupiter.api.Test;
import x.withlithum.neoware.test.NbtAssertions;
import x.withlithum.neoware.util.math.Vector2F;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static x.withlithum.neoware.test.ResultAssertions.*;

public class JavaCodecTest {
    @Test
    void testEncodeVector2F() {
        // Arrange
        final var input = new Vector2F(12.34F, 56.78F);

        // Act
        final var result = Vector2F.CODEC.encode(Transcoder.NBT, input);

        // Assert
        final var list = assertInstanceOf(ListBinaryTag.class,
            assertResultOk(result));
        assertAll(() -> NbtAssertions.assertFloat(12.34F, list.get(0)),
            () -> NbtAssertions.assertFloat(56.78F, list.get(1)));
    }

    @Test
    void testDecodeVector2F() {
        // Arrange
        final var input = ListBinaryTag.listBinaryTag(BinaryTagTypes.FLOAT,
            List.of(FloatBinaryTag.floatBinaryTag(123.123F),
                FloatBinaryTag.floatBinaryTag(456.456F)));

        // Act
        final var result = Vector2F.CODEC.decode(Transcoder.NBT, input);

        // Assert
        assertEquals(new Vector2F(123.123F, 456.456F),
            assertResultOk(result));
    }
}
