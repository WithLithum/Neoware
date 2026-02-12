/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content

import okio.FileSystem
import okio.Path
import x.withlithum.neoware.data.content.hierarchy.ContentTreeFactory

object AdventureContentTreeFactory : ContentTreeFactory<AdventureContentTree> {
    override val empty: AdventureContentTree
        get() = AdventureContentTree.EMPTY

    override val dataVersion = 1

    override fun loadDir(
        dir: Path,
        fs: FileSystem
    ): AdventureContentTree {
        return AdventureContentTree.loadDir(dir, fs)
    }
}