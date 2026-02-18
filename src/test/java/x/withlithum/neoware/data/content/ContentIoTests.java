/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content;

import org.junit.jupiter.api.Test;
import x.withlithum.neoware.data.content.io.ContentIo;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentIoTests {
    @Test
    void testFilePathToKeyPath() {
        // Arrange
        final var root = Path.of(System.getProperty("user.home"));
        final var relative = root.resolve("sub").resolve("test.txt");

        // Act
        final var result = ContentIo.keyPathFromFilePath(relative, root);

        // Assert
        assertEquals("sub/test", result);
    }
}
