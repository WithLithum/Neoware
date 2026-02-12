/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io

import io.github.oshai.kotlinlogging.KotlinLogging
import net.kyori.adventure.key.Key
import okio.FileSystem
import okio.IOException
import okio.buffer
import x.withlithum.neoware.data.content.ContentLoader
import x.withlithum.neoware.util.io.NPath
import x.withlithum.neoware.util.io.isRegularFile

object FileSystemTraverser {
    private val logger = KotlinLogging.logger {}

    fun <V> traverse(
        fs: FileSystem,
        directory: okio.Path,
        namespace: String,
        loader: ContentLoader<V>,
        storeInto: MutableMap<Key, V>,
    ) {
        fs.listRecursively(directory, false).forEach f@{ path ->
            val metadata = fs.metadata(path)
            if (!metadata.isRegularFile) {
                return@f
            }

            val extension = NPath.getExtension(path)
            if (extension != loader.acceptsExtension) {
                logger.debug { "Ignoring file $path because it is not of the supported extension '${loader.acceptsExtension}'" }
                return@f
            }

            val itemPath = ContentIo.createKeyPath(path, directory)
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
                            storeInto[key] = result.getOrThrow()
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