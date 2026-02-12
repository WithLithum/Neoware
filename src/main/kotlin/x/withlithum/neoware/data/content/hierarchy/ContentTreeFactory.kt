/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.hierarchy

import okio.FileSystem
import okio.Path

interface ContentTreeFactory<V> where V: IContentTree<V> {
    val empty: V
    val dataVersion: Int

    fun loadDir(dir: Path, fs: FileSystem): V;
}