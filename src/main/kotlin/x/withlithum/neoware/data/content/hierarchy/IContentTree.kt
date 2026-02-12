/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy

interface IContentTree {
    /**
     * Merges the current content tree with the other specified content tree. The contents of the
     * other content tree will override the current one if there is a conflict.
     *
     * @return The merged content tree.
     * @throws IllegalArgumentException The specified content tree is not supported.
     */
    fun merge(other: IContentTree): IContentTree
}