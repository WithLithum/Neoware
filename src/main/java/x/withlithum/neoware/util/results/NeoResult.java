/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results;

import lombok.Getter;
import net.minestom.server.codec.Result;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Defines a pattern, similar to a discriminated union, that can be used to report the status and
 * transmit the result of an operation.
 * @param <V> The type of the result value.
 */
@NullMarked
public abstract sealed class NeoResult<V> permits NeoResult.Error, NeoResult.Ok {
    /**
     * Indicates a successful status, and encapsulates the result of a succeeding operation.
     * @param <V> The type of the value.
     */
    public static final class Ok<V> extends NeoResult<V> {
        @Getter
        private final V value;

        private Ok(V value) {
            this.value = value;
        }

        @Override
        public V unwrap() {
            return value;
        }

        @Override
        public <R> NeoResult<R> map(NeoMapFunction<V, R> mapper) {
            return mapper.apply(value);
        }
    }

    /**
     * Indicates a failure status.
     * @param <V> The result value type.
     */
    public static final class Error<V> extends NeoResult<V> {
        @Getter
        private final String message;
        @Getter
        @Nullable
        private final Throwable cause;

        private Error(String message, @Nullable Throwable cause) {
            this.message = message;
            this.cause = cause;
        }

        @Override
        public V unwrap() {
            throw new NeoResultException(message, cause);
        }

        @Override
        public <R> NeoResult<R> map(NeoMapFunction<V, R> mapper) {
            return cast();
        }

        /**
         * Returns a new result instance, with the same message and cause (if any), but with a
         * different type.
         * @return The new instance
         * @param <R> The type to cast to.
         */
        @Contract("-> new")
        public <R> NeoResult<R> cast() {
            return new NeoResult.Error<>(message, cause);
        }

        /**
         * Logs a message, at warning level, with the message and the cause of this failure result.
         * @param logger The logger to log to.
         */
        public void logWarn(Logger logger) {
            if (logger.isWarnEnabled()) {
                if (cause != null) {
                    logger.warn(message, cause);
                } else {
                    logger.warn(message);
                }
            }
        }
    }

    /**
     * Asserts the current instance does not indicate failure. If it does, throws
     * {@link NeoResultException}.
     *
     * @return The value encapsulated within a successful result.
     */
    public abstract V unwrap();

    /**
     * If this instance indicates success, returns the result of the mapper function; otherwise,
     * returns a new result instance indicating an error, with the same message (and cause, if
     * available).
     *
     * @param mapper The mapper function.
     * @return The new result instance.
     * @param <R> The type to map to.
     */
    public abstract <R> NeoResult<R> map(NeoMapFunction<V, R> mapper);

    /**
     * Creates a new result instance that indicate success, with a value encapsulated within.
     * @param result The value.
     * @return A new instance of {@link Ok} with the specified value.
     * @param <V> The type of the value.
     */
    public static <V> NeoResult<V> ok(V result) {
        return new Ok<>(result);
    }

    /**
     * Creates a new result instance that indicates failure, with the specified message.
     * @param message The error message.
     * @return A new instance of {@link Error}.
     * @param <V> The result type.
     */
    public static <V> NeoResult<V> error(String message) {
        return error(message, null);
    }

    /**
     * Creates a new result instance that indicates failure, with the specified message, and a
     * {@link Throwable} that is the cause of the failure.
     * @param message The error message.
     * @param cause The throwable that caused the failure.
     * @return A new instance of {@link Error}.
     * @param <V> The result type.
     */
    public static <V> NeoResult<V> error(String message, @Nullable Throwable cause) {
        return new Error<>(message, cause);
    }

    /**
     * Converts the specified Minestom codec result to a Neoware result type.
     * @param minestom The Minestom result.
     * @return The converted Neoware result.
     * @param <V> The type of the result.
     */
    public static <V> NeoResult<V> fromMinestom(Result<V> minestom) {
        return switch (minestom) {
            case Result.Ok<V> o -> ok(o.value());
            case Result.Error<V> e -> error(e.message());
        };
    }
}
