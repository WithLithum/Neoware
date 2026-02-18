/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.game.locale;

import org.junit.jupiter.api.Test;
import x.withlithum.neoware.game.locale.MapTranslator;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class MapTranslatorTest {
    @Test
    void testHasDefaultHasOverrideUseOverride() {
        // Arrange
        final var map = Map.of(
            "en-US", Map.of("foo", new MessageFormat("bar")),
            "en-GB", Map.of("foo", new MessageFormat("baz"))
        );
        final var translator = new MapTranslator(map);

        // Act
        final var result = translator.translate("foo", Locale.UK);

        // Assert
        assertNotNull(result);
        assertEquals("baz", result.toPattern());
    }

    @Test
    void testHasDefaultNoOverrideUseDefault() {
        // Arrange
        final var map = Map.of(
            "en-US", Map.of("foo", new MessageFormat("bar")),
            "en-GB", Map.of("baz", new MessageFormat("bar"))
        );
        final var translator = new MapTranslator(map);

        // Act
        final var result = translator.translate("foo", Locale.UK);

        // Assert
        assertNotNull(result);
        assertEquals("bar", result.toPattern());
    }

    @Test
    void testNoDefaultNoOverrideReturnNull() {
        // Arrange
        final var translator = new MapTranslator(Map.of());

        // Act
        final var result = translator.translate("foo", Locale.UK);

        // Assert
        assertNull(result);
    }
}
