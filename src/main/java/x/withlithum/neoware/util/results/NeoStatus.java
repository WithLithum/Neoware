/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public sealed abstract class NeoStatus permits NeoStatus.Ok, NeoStatus.Error {
    public static final class Ok extends NeoStatus {
        private static final Ok INSTANCE = new Ok();

        private Ok() {
        }

        @Override
        public <V> NeoResult<V> withValue(V value) {
            return NeoResult.ok(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Ok;
        }
    }

    public static final class Error extends NeoStatus {
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
        public void unwrap() {
            throw new NeoResultException(message, cause);
        }

        @Override
        public <V> NeoResult<V> withValue(V value) {
            return NeoResult.error(message, cause);
        }
    }

    /**
     * Returns an instance of {@link NeoStatus} indicating a successful result. This method always
     * returns the same instance (and thus, it is safe to do identity checks).
     */
    @Contract(pure = true)
    public static NeoStatus ok() {
        return Ok.INSTANCE;
    }

    /**
     * Returns a new result instance that indicates an error, with the specified error message.
     * @param message The message.
     * @return A new instance of {@link Error}.
     */
    public static NeoStatus error(String message) {
        return new Error(message, null);
    }

    /**
     * Returns a new result instance that indicates an error, with the specified error message and
     * an optional cause.
     * @param message The message.
     * @param cause The throwable that caused the failure.
     * @return A new instance of {@link Error}.
     */
    public static NeoStatus error(String message, @Nullable Throwable cause) {
        return new Error(message, cause);
    }

    /**
     * Asserts this instance is a successful status. If it is not, throws
     * {@link NeoResultException}.
     */
    public void unwrap() {
    }

    /**
     * Creates a new instance of {@link NeoResultException}, possibly containing the specified
     * value, corresponding to the current instance.
     *
     * @param value The value to encapsulate.
     * @param <V>   The type of the value to encapsulate.
     * @return An instance of {@link NeoResult} corresponding to the current instance.
     */
    public abstract <V> NeoResult<V> withValue(V value);
}
