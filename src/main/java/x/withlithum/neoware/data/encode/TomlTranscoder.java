package x.withlithum.neoware.data.encode;

import io.github.wasabithumb.jtoml.key.TomlKey;
import io.github.wasabithumb.jtoml.value.TomlValue;
import io.github.wasabithumb.jtoml.value.primitive.TomlPrimitive;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.*;

/// Providers codec support for TOML.
///
/// In order to behave correctly as a tree-structure codec, this transcoder prohibits the use of
/// deep keys (e.g. `table.value`).
///
/// This transcoder does not support conversion ([#convertTo(Transcoder, TomlValue)]), and `null`
/// values are represented as an empty table `{}`.
///
/// @see TomlValue
@NullMarked
public final class TomlTranscoder implements Transcoder<TomlValue> {
    public static final TomlTranscoder INSTANCE = new TomlTranscoder();

    private TomlTranscoder() {
    }

    @Override
    public TomlValue createNull() {
        return TomlTable.create(); // So why would you read a null? this is not supported
    }

    public static <V> Result.Error<V> errExpectPrimitive() {
        return new Result.Error<>("Expected primitive value");
    }

    public static <V> Result.Error<V> errExpectType(String type) {
        return new Result.Error<>(String.format("Expected '%s' value", type));
    }

    private Result<Integer> getIntWithRange(TomlValue value, int min, int max) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (!prim.isInteger()) {
            errExpectType("int");
        }

        var intValue = prim.asInteger();
        if (intValue <= min || intValue >= max) {
            return new Result.Error<>(String.format("Expected a value between %d and %d", min, max));
        }

        return new Result.Ok<>(intValue);
    }

    @Override
    public Result<Boolean> getBoolean(TomlValue value) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (!prim.isBoolean()) {
            return errExpectType("boolean");
        }

        return new Result.Ok<>(prim.asBoolean());
    }

    @Override
    public TomlValue createBoolean(boolean value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Byte> getByte(TomlValue value) {
        var intValue = getIntWithRange(value, Byte.MIN_VALUE, Byte.MAX_VALUE);
        if (intValue instanceof Result.Error<Integer>(String message)) {
            return new Result.Error<>(message);
        }

        return new Result.Ok<>(intValue.orElseThrow().byteValue());
    }

    @Override
    public TomlValue createByte(byte value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Short> getShort(TomlValue value) {
        var intValue = getIntWithRange(value, Short.MIN_VALUE, Short.MAX_VALUE);
        if (intValue instanceof Result.Error<Integer>(String message)) {
            return new Result.Error<>(message);
        }

        return new Result.Ok<>(intValue.orElseThrow().shortValue());
    }

    @Override
    public TomlValue createShort(short value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Integer> getInt(TomlValue value) {
        return getIntWithRange(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public TomlValue createInt(int value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Long> getLong(TomlValue value) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (!prim.isInteger()) {
            return errExpectType("long");
        }

        var longValue = prim.asLong();
        return new Result.Ok<>(longValue);
    }

    @Override
    public TomlValue createLong(long value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Float> getFloat(TomlValue value) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (prim.isInteger()) {
            return new Result.Ok<>((float) prim.asInteger());
        }

        if (!prim.isFloat()) {
            return errExpectType("float");
        }

        var result = prim.asFloat();
        return new Result.Ok<>(result);
    }

    @Override
    public TomlValue createFloat(float value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<Double> getDouble(TomlValue value) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (prim.isInteger()) {
            return new Result.Ok<>((double) prim.asInteger());
        }

        if (!prim.isFloat()) {
            return errExpectType("double");
        }

        var result = prim.asDouble();
        return new Result.Ok<>(result);
    }

    @Override
    public TomlValue createDouble(double value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<String> getString(TomlValue value) {
        if (!value.isPrimitive()) {
            return errExpectPrimitive();
        }

        var prim = value.asPrimitive();
        if (!prim.isString()) {
            return errExpectType("string");
        }

        var result = prim.asString();
        return new Result.Ok<>(result);
    }

    @Override
    public TomlValue createString(String value) {
        return TomlPrimitive.of(value);
    }

    @Override
    public Result<@Unmodifiable List<TomlValue>> getList(TomlValue value) {
        if (!value.isArray()) {
            return errExpectType("array");
        }

        var array = value.asArray();
        return new Result.Ok<>(Arrays.asList(array.toArray()));
    }

    @Override
    public ListBuilder<TomlValue> createList(int expectedSize) {
        return new TomlArrayBuilder(expectedSize);
    }

    @Override
    public Result<MapLike<TomlValue>> getMap(TomlValue value) {
        if (!value.isTable()) {
            return errExpectType("table");
        }

        var table = value.asTable();

        var keyList = table.keys(false).stream().map(TomlKey::toString)
            .toList();

        return new Result.Ok<>(new MapLike<>()
        {
            @Override
            public @Unmodifiable Collection<String> keys() {
                return keyList;
            }

            @Override
            public boolean hasValue(String key) {
                return keyList.contains(key);
            }

            @Override
            public Result<TomlValue> getValue(String key) {
                var value = table.get(key);
                if (value == null || !keyList.contains(key)) {
                    return new Result.Error<>("key " + key + " not found");
                }

                return new Result.Ok<>(value);
            }
        });
    }

    @Override
    public MapBuilder<TomlValue> createMap() {
        return new TomlMapBuilder();
    }

    @Override
    public <O> Result<O> convertTo(Transcoder<O> coder, TomlValue value) {
        throw new UnsupportedOperationException("Intermediary value conversion is not implemented at this time. :(");
    }
}
