/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import x.withlithum.neoware.data.content.hierarchy.ContentTreeFactory
import x.withlithum.neoware.util.results.NeoResult
import java.nio.file.Path

object AdventureContentTreeFactory : ContentTreeFactory<AdventureContentTree> {
    override fun getEmpty() = AdventureContentTree.EMPTY
    override fun getDataVersion() = 1

    override fun loadDir(
        dir: Path
    ): NeoResult<AdventureContentTree> {
        return AdventureContentTree.loadDir(dir.toOkioPath(), FileSystem.SYSTEM)
    }
}