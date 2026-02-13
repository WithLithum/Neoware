/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy;

import org.jetbrains.annotations.CheckReturnValue;

/**
 * Defines a content tree that supports merging.
 * @param <TSelf> The implementing type.
 */
public interface ContentTree<TSelf extends ContentTree<TSelf>> {
    /**
     * Merges the current instance with the other instance. Contents in the latter wins if
     * conflicting with the former.
     * @param other The other content tree.
     * @return The merged content tree.
     */
    @CheckReturnValue
    TSelf merge(TSelf other);
}
