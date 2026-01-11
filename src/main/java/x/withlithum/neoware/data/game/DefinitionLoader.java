package x.withlithum.neoware.data.game;

import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.except.parse.TomlLocalParseException;
import io.github.wasabithumb.jtoml.except.parse.TomlParseException;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import net.minestom.server.codec.Result;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.encode.TomlTranscoder;
import x.withlithum.neoware.game.item.DefinitionPrototype;
import x.withlithum.neoware.game.item.ItemPrototype;
import x.withlithum.neoware.util.KeyRoot;
import x.withlithum.neoware.util.ResourceRef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public final class DefinitionLoader {
    private DefinitionLoader() {}

    private static final String PREFIX = "neoware/item";
    private static final JToml TOML = JToml.jToml();

    private static List<ResourceRef> getResourceFiles(ClassLoader classLoader) {
        List<ResourceRef> filenames = new ArrayList<>();
        var builder = new StringBuilder();

        try (
            InputStream in = classLoader.getResourceAsStream(PREFIX)) {
            if (in == null) {
                return List.of();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;

                while ((resource = br.readLine()) != null) {
                    builder.delete(0, builder.length());
                    builder.ensureCapacity(PREFIX.length() + resource.length() + 1);
                    builder.append(PREFIX);
                    builder.append('/');
                    builder.append(resource);

                    var url = classLoader.getResource(builder.toString());
                    if (url != null) {
                        filenames.add(new ResourceRef(resource, url));
                    } else {
                        log.info("Constructed invalid resource name: {}", builder);
                    }
                }
            }
        } catch (IOException ex) {
            log.warn("Failed to read from items directory", ex);
            return List.of();
        }

        return filenames;
    }

    public static @Unmodifiable Map<Key, ItemPrototype> loadItems() {
        var classLoader = Thread.currentThread().getContextClassLoader();
        var fileList = getResourceFiles(classLoader);

        var map = new HashMap<Key, ItemPrototype>();
        var toml = JToml.jToml();
        for (var current : fileList) {
            try (final var stream = current.url().openStream()) {
                processResource(current, stream, map);
            } catch (IOException e) {
                log.error("Failed to load resource", e);
            } catch (TomlLocalParseException e) {
                log.error("TOML format error: {}: ({}, {}): {}",
                    current.name(),
                    e.getLineNumber(),
                    e.getColumnNumber(),
                    e.getRawMessage());
            } catch (TomlParseException e) {
                log.error("Failed to parse TOML resource {}: {}",
                    current.name(),
                    e.getMessage());
            }
        }

        log.info("{} item definitions loaded", map.size());
        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("PatternValidation")
    private static @Nullable Key createId(String fileName) {
        var path = fileName.substring(0, fileName.lastIndexOf('.'));
        if (!Key.parseableValue(path)) {
            log.warn("Invalid resource path name: {}", fileName);
            return null;
        }

        return KeyRoot.id(path);
    }

    @SuppressWarnings("PatternValidation")
    private static void processResource(ResourceRef ref,
                                        InputStream stream,
                                        HashMap<Key, ItemPrototype> map) {
        TomlTable tomlTable;
        tomlTable = TOML.read(stream);

        var decodeResult = ItemDefinition.CODEC.decode(TomlTranscoder.INSTANCE,
            tomlTable);

        var key = createId(ref.name());
        if (key == null) {
            return;
        }

        switch (decodeResult) {
            case Result.Ok(ItemDefinition v) -> map.put(KeyRoot.id(ref.name()),
                new DefinitionPrototype(v));
            case Result.Error(String message) -> log.warn("Failed to decode resource '{}': {}",
                key.asString(),
                message);
        }
    }
}
