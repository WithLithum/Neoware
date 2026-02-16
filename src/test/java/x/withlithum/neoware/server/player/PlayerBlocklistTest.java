/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.player;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerBlocklistTest {
    @Test
    void testExpiredLookupReturnsNull() {
        // Arrange
        final var uuid = UUID.randomUUID();
        final var blocklist = new PlayerBlocklistImpl(Path.of("test-data/player-blocklist.json"));
        blocklist.insert(uuid, null, Instant.now().minusSeconds(10));

        // Act
        final var result = blocklist.lookup(uuid);

        // Assert
        assertNull(result);
    }
}
