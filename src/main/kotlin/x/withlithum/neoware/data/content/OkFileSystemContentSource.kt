/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.key.Key
import okio.FileSystem
import okio.IOException
import okio.Path
import okio.buffer
import x.withlithum.neoware.util.io.isDirectory
import x.withlithum.neoware.util.io.isRegularFile

class OkFileSystemContentSource(val base: Path,
    val fs: FileSystem): ContentSource {
    companion object {
        private val logger = KotlinLogging.logger {}

        fun createKeyPath(relative: Path, root: Path): String {
            val itemPath = relative.relativeTo(root).toString()
                .replace(Path.DIRECTORY_SEPARATOR, "/")
            return itemPath.substring(0, itemPath.lastIndexOf('.'))
        }
    }

    override fun <V> loadContents(
        rootName: String,
        loader: ContentLoader<V>
    ): Map<Key, V> {
        if (!Key.parseableValue(rootName)) {
            throw IllegalArgumentException("$rootName is not a valid root name")
        }

        val rootDir = base.resolve(rootName)
        if (!fs.isDirectory(rootDir)) {
            logger.warn { "Category root '$rootName' is not a directory" }
            return emptyMap()
        }

        val map = HashMap<Key, V>()
        fs.list(rootDir).forEach f@{ path ->
            val nsMeta = fs.metadataOrNull(path)
            if (nsMeta == null || !nsMeta.isDirectory) {
                logger.warn {"Skipping '${path.name}': not a directory" }
                return@f
            }

            val ns = path.relativeTo(base).toString()
            if (!Key.parseableNamespace(ns)) {
                logger.warn {"Skipping '$ns': not a valid namespace" }
                return@f
            }

            resolveNamespace(ns, rootDir, map, loader)
        }

        return map
    }

    private fun <V> resolveNamespace(namespace: String,
                                     rootPath: Path,
                                     target: MutableMap<Key, V>,
                                     loader: ContentLoader<V>) {
        fs.listRecursively(rootPath, false).forEach f@{ path ->
            if (!fs.isRegularFile(path)) {
                return@f
            }

            val itemPath = createKeyPath(path, rootPath)
            if (!Key.parseableValue(itemPath)) {
                logger.warn {"Skipping '${itemPath} in ${namespace}': not a valid file name" }
                return@f
            }

            val key = Key.key(namespace, itemPath)
            try {
                fs.openReadOnly(path).use { fh ->
                    val source = fh.source()
                    source.buffer().inputStream().use {
                        val result = loader.load(it)
                        if (result.isSuccess) {
                            target[key] = result.getOrThrow()
                        } else {
                            logger.warn(result.exceptionOrNull()) { "Failed to load content '${key.asString()}'" }
                        }
                    }
                }
            } catch (e: IOException) {
                logger.warn(e) { "Failed to load content '${key.asString()}'" }
            }
        }
    }
}