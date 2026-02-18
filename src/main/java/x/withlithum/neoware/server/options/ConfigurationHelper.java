/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.options;

import io.github.wasabithumb.jtoml.configurate.TomlConfigurationLoader;
import io.github.wasabithumb.jtoml.except.parse.TomlParseException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@NullMarked
@Slf4j
public final class ConfigurationHelper {
    private final ConfigurationNode node;

    private ConfigurationHelper() {
        throw new AssertionError("No ConfigurationHelper instances for you!");
    }

    public static ConfigurationNode load(Path localPath) {
        final var loader = TomlConfigurationLoader.builder()
            .path(localPath)
            .build();

        var save = false;
        if (!Files.isRegularFile(localPath)) {
            log.warn("Configuration is not a file: {}", localPath);
            save = true;
        }

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (TomlParseException | IOException e) {
            log.warn("Failed to load configuration", e);
            root = CommentedConfigurationNode.root();
        }

        if (save) {
            try {
                loader.save(root);
            } catch (IOException e) {
                log.warn("Failed to save configuration", e);
            }
        }

        return root;
    }
}
