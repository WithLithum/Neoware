/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.key.Key
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

class FileSystemContentSource(val baseDirectory: Path): ContentSource {
    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun createKeyPath(relativePath: Path,
                             rootPath: Path): String {
            var itemPath = rootPath.relativize(relativePath).toString()
                .replace(File.separatorChar, '/')
            itemPath = itemPath.substring(0, itemPath.lastIndexOf('.'))

            return itemPath
        }
    }

    override fun <V> loadContents(rootName: String, loader: ContentLoader<V>): Map<Key, V> {
        if (!Key.parseableValue(rootName)) {
            throw IllegalArgumentException("$rootName is not a valid root name")
        }

        val rootDir = baseDirectory.resolve(rootName)
        if (!Files.isDirectory(rootDir)) {
            LOGGER.warn { "$rootName is not a directory" }
            return mapOf()
        }

        val map = HashMap<Key, V>()
        Files.newDirectoryStream(rootDir).use { stream ->
            stream.forEach f@{
                if (!Files.isDirectory(it)) {
                    return@f
                }

                val nameSpace = it.relativize(rootDir).toString()
                if (!Key.parseableNamespace(nameSpace)) {
                    LOGGER.warn { "$rootName: Encountered subdirectory '$nameSpace' which is not a valid namespace" }
                    return@f
                }

                resolveNamespace(nameSpace, it, map, loader)
            }
        }

        return map
    }

    private fun <V> resolveNamespace(
        namespace: String,
        rootPath: Path,
        target: MutableMap<Key, V>,
        loader: ContentLoader<V>
    ) {
        Files.walk(rootPath).use {
            it.forEach f@{ file ->
                if (!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
                    return@f
                }

                val itemPath = createKeyPath(file, rootPath)
                if (!Key.parseableValue(itemPath)) {
                    LOGGER.warn { "Malformed path: $itemPath" }
                    return@f
                }

                val key = Key.key(namespace, itemPath)

                try {
                    Files.newInputStream(file).use { stream ->
                        val result = loader.load(stream)
                        if (result.isSuccess) {
                            target[key] = result.getOrThrow()
                        } else {
                            LOGGER.warn(result.exceptionOrNull()) { "Failed to load content '${key.asString()}'" }
                        }
                    }
                } catch (e: IOException) {
                    LOGGER.warn(e) { "Failed to load content '${key.asString()}'" }
                }
            }
        }
    }
}