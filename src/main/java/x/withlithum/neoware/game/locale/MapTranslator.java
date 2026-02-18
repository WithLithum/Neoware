/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.game.locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.util.KeyRoot;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

@NullMarked
public final class MapTranslator implements Translator {
    private static final String US_LANGUAGE = Locale.US.toLanguageTag();

    private final Map<String, Map<String, MessageFormat>> data;

    public MapTranslator(Map<String, Map<String, MessageFormat>> data) {
        this.data = data;
    }

    @Override
    public Key name() {
        return KeyRoot.id("map_based");
    }

    @Override
    public @Nullable MessageFormat translate(String key, Locale locale) {
        final var langTag = locale.toLanguageTag();
        var langDict = data.get(langTag);
        if (langDict == null || !langDict.containsKey(key)) {
            langDict = data.get(US_LANGUAGE);
        }

        if (langDict == null) {
            return null;
        }

        return langDict.get(key);
    }
}
