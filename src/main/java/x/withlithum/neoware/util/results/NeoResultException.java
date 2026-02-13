/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

public class NeoResultException extends RuntimeException {
    @ApiStatus.Internal
    public NeoResultException(String message, @Nullable  Throwable cause) {
        super(message, cause);
    }
}
