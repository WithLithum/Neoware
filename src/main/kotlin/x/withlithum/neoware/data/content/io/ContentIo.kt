/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io

import net.kyori.adventure.key.Key
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.data.content.ContentLoader
import x.withlithum.neoware.data.content.OkFileSystemContentSource
import x.withlithum.neoware.util.io.isDirectory

object ContentIo {
    fun <V> traverse(basePath: Path,
                     contentType: String,
                     fs: FileSystem,
                     loader: ContentLoader<V>): Map<Key, V> {
        if (!fs.isDirectory(basePath)) {
            return emptyMap()
        }

        val map = HashMap<Key, V>()
        fs.list(basePath).forEach f@{
            if (!fs.isDirectory(it) || !Key.parseableNamespace(it.name)) {
                return@f
            }

            val typeFolder = it.resolve(contentType)
            if (!fs.isDirectory(typeFolder)) {
                return@f
            }

            OkFileSystemContentSource(typeFolder, fs).traverse(it.name, loader, map)
        }

        return map
    }
}