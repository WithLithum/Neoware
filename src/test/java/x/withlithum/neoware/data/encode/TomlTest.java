package x.withlithum.neoware.data.encode;

import io.github.wasabithumb.jtoml.value.primitive.TomlPrimitive;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import org.junit.jupiter.api.Test;

import static x.withlithum.neoware.test.ResultAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class TomlTest {
    @Test
    void testTranscoderCreateString() {
        // Arrange
        final var transcoder = TomlTranscoder.INSTANCE;
        final var input = "Test string";

        // Act
        final var result = transcoder.createString(input);

        // Assert
        assertTrue(result.isPrimitive());
        assertTrue(result.asPrimitive().isString());
    }

    @Test
    void testTranscoderGetString() {
        // Arrange
        final var input = TomlPrimitive.of("Lorem ipsum");

        // Act
        final var result = TomlTranscoder.INSTANCE.getString(input);

        // Assert
        final var value = assertResultOk(result);
        assertEquals("Lorem ipsum", value);
    }

    @Test
    void testTranscoderTableGetDeepFails() {
        // Arrange
        final var table = TomlTable.create();
        table.put("test_value", TomlPrimitive.of("Shallow value"));
        table.put("deep.test_value", TomlPrimitive.of("Deep value"));
        final var codecTable = TomlTranscoder.INSTANCE.getMap(table);

        // Act
        var codecMap = assertResultOk(codecTable);
        var shallowValue = codecMap.getValue("test_value");
        var deepValue = codecMap.getValue("deep.test_value");

        // Assert
        assertResultOk(shallowValue);
        assertResultError(deepValue);
    }

    @Test
    void testTranscoderTableNoListDeepKeys() {
        // Arrange
        final var table = TomlTable.create();
        table.put("shallow_key", TomlPrimitive.of("Shallow value"));
        table.put("deep.key", TomlPrimitive.of("Deep value"));
        final var codecTable = assertResultOk(TomlTranscoder.INSTANCE.getMap(table));

        // Act
        final var keyList = codecTable.keys();

        // Assert
        assertTrue(keyList.contains("shallow_key"));
        assertFalse(keyList.contains("deep.key"));
    }

    @Test
    void testTranscoderTableNoListDeepKeysButListTable() {
        // Arrange
        final var table = TomlTable.create();
        table.put("deep.key", TomlPrimitive.of("Deep value"));
        final var codecTable = assertResultOk(TomlTranscoder.INSTANCE.getMap(table));

        // Act
        final var keyList = codecTable.keys();

        // Assert
        assertTrue(keyList.contains("deep"));
        assertFalse(keyList.contains("deep.key"));
    }

    @Test
    void testTranscoderTableNoContainDeepKeys() {
        // Arrange
        final var table = TomlTable.create();
        table.put("shallow_key", TomlPrimitive.of("Shallow value"));
        table.put("deep.key", TomlPrimitive.of("Deep value"));
        final var codecTable = assertResultOk(TomlTranscoder.INSTANCE.getMap(table));

        // Act
        final var containsShallow = codecTable.hasValue("shallow_key");
        final var containsDeep = codecTable.hasValue("deep.key");

        // Assert
        assertTrue(containsShallow);
        assertFalse(containsDeep);
    }

    @Test
    void testTranscoderTableNoContainDeepKeysButHaveTable() {
        // Arrange
        final var table = TomlTable.create();
        table.put("deep.key", TomlPrimitive.of("Deep value"));
        final var codecTable = assertResultOk(TomlTranscoder.INSTANCE.getMap(table));

        // Act
        final var containsTable = codecTable.hasValue("deep");
        final var containsDeep = codecTable.hasValue("deep.key");

        // Assert
        assertTrue(containsTable);
        assertFalse(containsDeep);
    }
}
