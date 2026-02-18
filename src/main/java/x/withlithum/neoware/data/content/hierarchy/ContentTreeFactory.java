/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy;

import x.withlithum.neoware.util.results.NeoResult;

import java.nio.file.Path;

public interface ContentTreeFactory<V extends ContentTree<V>> {
    V getEmpty();
    int getDataVersion();

    NeoResult<V> loadDir(Path directory);
}
