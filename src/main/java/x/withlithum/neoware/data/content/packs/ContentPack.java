/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs;

import x.withlithum.neoware.data.content.hierarchy.ContentTree;

public record ContentPack<V extends ContentTree<V>>(ContentPackMeta meta,
                                                    V tree) {
}
