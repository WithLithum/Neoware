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
import x.withlithum.neoware.util.results.NeoResult

object FileSystemTraverser {
    private val logger = KotlinLogging.logger {}

    fun <V> traverse(
        fs: FileSystem,
        directory: okio.Path,
        namespace: String,
        decoder: ContentDecoder<V>,
        storeInto: MutableMap<Key, V>,
    ) {
        fs.listRecursively(directory, false).forEach f@{ path ->
            val metadata = fs.metadata(path)
            if (!metadata.isRegularFile) {
                return@f
            }

            val extension = path.toFile().extension
            if (extension != decoder.acceptedExtension) {
                logger.debug { "Ignoring file $path because it is not of the supported extension '${decoder.acceptedExtension}'" }
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
                        when (val result = decoder.load(it)) {
                            is NeoResult.Ok -> storeInto[key] = result.value
                            is NeoResult.Error -> logger.warn(result.cause) { "Failed to decode content: ${result.message}" }
                        }
                    }
                }
            } catch (e: IOException) {
                logger.warn(e) { "Failed to load content '${key.asString()}'" }
            }
        }
    }
}