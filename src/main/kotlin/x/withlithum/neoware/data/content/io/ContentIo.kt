/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io

import net.kyori.adventure.key.Key
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.util.io.isDirectory

object ContentIo {
    fun <V> traverse(basePath: Path,
                     contentType: String,
                     fs: FileSystem,
                     loader: ContentDecoder<V>): Map<Key, V> {
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

            FileSystemTraverser.traverse(FileSystem.SYSTEM,
                it,
                it.name,
                loader,
                map)
        }

        return map
    }

    fun createKeyPath(relative: Path, root: Path): String {
        val itemPath = relative.relativeTo(root).toString()
            .replace(Path.DIRECTORY_SEPARATOR, "/")
        return itemPath.substring(0, itemPath.lastIndexOf('.'))
    }
}