/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.results;

@FunctionalInterface
public interface NeoMapFunction<V, R> {
    NeoResult<R> apply(V value);
}
