package x.withlithum.neoware.data.encode;

import io.github.wasabithumb.jtoml.value.TomlValue;
import io.github.wasabithumb.jtoml.value.array.TomlArray;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TomlArrayBuilder implements Transcoder.ListBuilder<TomlValue> {
    private final TomlArray array;

    public TomlArrayBuilder(int expectedSize) {
        this.array = TomlArray.create(expectedSize);
    }

    @Override
    public Transcoder.ListBuilder<TomlValue> add(TomlValue value) {
        array.add(value);
        return this;
    }

    @Override
    public TomlValue build() {
        return array;
    }
}
