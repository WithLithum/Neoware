/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.commands;

import net.minestom.server.codec.Transcoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import x.withlithum.neoware.test.ResultAssertions;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionRankTest {
    @Test
    void testPermissionRankLargerThanWheel() {
        // Arrange
        final var input = 100;

        // Act
        final var result = PermissionRank.fromValue(input);

        // Assert
        assertEquals(PermissionRank.WHEEL, result);
    }

    @Test
    void testPermissionRankSmallerThanGuest() {
        // Arrange
        final var input = -999;

        // Act
        final var result = PermissionRank.fromValue(input);

        // Assert
        assertEquals(PermissionRank.GUEST, result);
    }

    @ParameterizedTest
    @EnumSource(PermissionRank.class)
    void testPermissionRankValueOfMatches(PermissionRank rank) {
        // Arrange
        final var value = rank.getValue();

        // Act
        final var result = PermissionRank.fromValue(value);

        // Assert
        assertEquals(rank, result);
    }

    @ParameterizedTest
    @EnumSource(PermissionRank.class)
    void testPermissionRankCodecMatches(PermissionRank rank) {
        // Arrange
        final var value = rank.getValue();

        // Act
        final var result = PermissionRank.CODEC.decode(Transcoder.JAVA, value);

        // Assert
        final var resultValue = ResultAssertions.assertResultOk(result);
        assertEquals(rank, resultValue);
    }
}
