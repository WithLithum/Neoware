/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.encode

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import org.jetbrains.annotations.Unmodifiable

object KJsonTranscoder : Transcoder<JsonElement> {
    override fun createNull(): JsonElement {
        return JsonNull
    }

    override fun getBoolean(value: JsonElement): Result<Boolean> {
        if (value !is JsonPrimitive) {
            return Result.Error("Expected JSON boolean")
        }

        val result = value.booleanOrNull ?: return Result.Error("Expected JSON boolean")
        return Result.Ok(result)
    }

    override fun createBoolean(value: Boolean): JsonElement {
        return JsonPrimitive(value)
    }

    private fun getIntegerOfRange(value: JsonElement, min: Int, max: Int): Result<Int> {
        if (value !is JsonPrimitive) {
            return errExpectedNumber()
        }

        val result = value.intOrNull ?: return Result.Error("Expected JSON integral number")
        if (result !in min..max) {
            return Result.Error("Expected a number between $min and $max")
        }

        return Result.Ok(result)
    }

    override fun getByte(value: JsonElement): Result<Byte> {
        val num = getIntegerOfRange(value, Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt())
        return num.kMapResult { it.toByte() }
    }

    override fun createByte(value: Byte): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getShort(value: JsonElement): Result<Short> {
        val num = getIntegerOfRange(value, Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
        return num.kMapResult { it.toShort() }
    }

    override fun createShort(value: Short): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getInt(value: JsonElement): Result<Int> {
        if (value !is JsonPrimitive) {
            return errExpectedNumber()
        }

        val result = value.intOrNull ?: return Result.Error("Expected an integer")
        return Result.Ok(result)
    }

    override fun createInt(value: Int): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getLong(value: JsonElement): Result<Long> {
        if (value !is JsonPrimitive) {
            return errExpectedNumber()
        }

        val result = value.longOrNull ?: return Result.Error("Expected a long integer")
        return Result.Ok(result)
    }

    override fun createLong(value: Long): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getFloat(value: JsonElement): Result<Float> {
        if (value !is JsonPrimitive) {
            return errExpectedNumber()
        }

        val result = value.floatOrNull ?: return Result.Error("Expected a floating point number")
        return Result.Ok(result)
    }

    override fun createFloat(value: Float): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getDouble(value: JsonElement): Result<Double> {
        if (value !is JsonPrimitive) {
            return errExpectedNumber()
        }

        val result = value.doubleOrNull ?: return Result.Error("Expected a floating point number")
        return Result.Ok(result)
    }

    override fun createDouble(value: Double): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getString(value: JsonElement): Result<String> {
        if (value !is JsonPrimitive || !value.isString) {
            return Result.Error("Expected a string")
        }

        return Result.Ok(value.content)
    }

    override fun createString(value: String): JsonElement {
        return JsonPrimitive(value)
    }

    override fun getList(value: JsonElement): Result<@Unmodifiable List<JsonElement>> {
        if (value !is JsonArray) {
            return Result.Error("Expected an JSON array")
        }

        return Result.Ok(value)
    }

    override fun createList(expectedSize: Int): Transcoder.ListBuilder<JsonElement> {
        return KJsonListBuilder(expectedSize)
    }

    override fun getMap(value: JsonElement): Result<Transcoder.MapLike<JsonElement>> {
        if (value !is JsonObject) {
            return Result.Error("Expected a JSON object")
        }

        return Result.Ok(KJsonMapLike(value))
    }

    override fun createMap(): Transcoder.MapBuilder<JsonElement> {
        return KJsonMapBuilder()
    }

    override fun <O> convertTo(
        coder: Transcoder<O>,
        value: JsonElement
    ): Result<O> {
        return Result.Error("JSON codec does not support conversion. Sorry!")
    }
}