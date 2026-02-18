/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {
    @Test
    void testStatusOkIdentity() {
        final var statusA = NeoStatus.ok();
        final var statusB = NeoStatus.ok();

        assertSame(statusA, statusB);
    }

    @Test
    void testStatusError() {
        final var status = NeoStatus.error("Error");

        assertInstanceOf(NeoStatus.Error.class, status);
    }

    @Test
    void testResultOk() {
        final var result = NeoResult.ok("This is a result value");

        assertInstanceOf(NeoResult.Ok.class, result);
    }

    @Test
    void testResultError() {
        final var result = NeoResult.error("Error");

        assertInstanceOf(NeoResult.Error.class, result);
    }

    @Test
    void testStatusOkUnwrap() {
        final var status = NeoStatus.ok();

        assertDoesNotThrow(status::unwrap);
    }

    @Test
    void testStatusErrorUnwrap() {
        final var status = NeoStatus.error("Error");

        assertThrows(NeoResultException.class, status::unwrap);
    }

    @Test
    void testResultOkUnwrap() {
        final var result = NeoResult.ok("This is a result value");

        final var value = assertDoesNotThrow(result::unwrap);
        assertEquals("This is a result value", value);
    }

    @Test
    void testResultErrorUnwrap() {
        final var result = NeoResult.error("Error");

        assertThrows(NeoResultException.class, result::unwrap);
    }

    @Test
    void testResultOkMap() {
        // Arrange
        final var from = NeoResult.ok("This is a result value");

        // Act
        final var to = from.map(_ -> NeoResult.ok(123));

        // Assert
        final var ok = assertInstanceOf(NeoResult.Ok.class, to);
        assertEquals(123, ok.getValue());
    }

    @Test
    void testResultErrorMap() {
        // Arrange
        final var from = NeoResult.error("Error");

        // Act
        final var to = from.map(_ -> NeoResult.ok(123));

        // Assert
        assertInstanceOf(NeoResult.Error.class, to);
    }
}
