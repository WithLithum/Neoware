package x.withlithum.neoware.data.encode;

import io.github.wasabithumb.jtoml.key.TomlKey;
import io.github.wasabithumb.jtoml.value.TomlValue;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TomlMapBuilder implements Transcoder.MapBuilder<TomlValue> {
    private final TomlTable table = TomlTable.create();

    @Override
    public Transcoder.MapBuilder<TomlValue> put(TomlValue key, TomlValue value) {
        if (!key.isPrimitive()) {
            throw new IllegalArgumentException("Key must be a string.");
        }

        final var strKey = key.asPrimitive();
        if (!strKey.isString()) {
            throw new IllegalArgumentException("Key must be a string.");
        }

        return put(strKey.asString(), value);
    }

    @Override
    public Transcoder.MapBuilder<TomlValue> put(String key, TomlValue value) {
        var tomlKey = TomlKey.parse(key);
        if (tomlKey.size() != 1) {
            throw new IllegalArgumentException("Key indicates a nested value which is not allowed here.");
        }

        table.put(key, value);
        return this;
    }

    @Override
    public TomlValue build() {
        return table;
    }
}
