/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.wasabithumb.jtoml.JToml
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.data.content.hierarchy.ContentTree
import x.withlithum.neoware.data.content.hierarchy.ContentTreeFactory
import x.withlithum.neoware.data.encode.TomlTranscoder
import x.withlithum.neoware.util.io.isDirectory
import x.withlithum.neoware.util.io.isRegularFile
import x.withlithum.neoware.util.io.read
import net.minestom.server.codec.Result as MResult

object ContentPackLoader {
    const val PACK_META_FILE = "neoware_pack.toml"
    const val PACK_DATA_DIR = "data"

    private val logger = KotlinLogging.logger {}
    private val toml = JToml.jToml()

    fun <V> loadAll(path: Path, fs: FileSystem, factory: ContentTreeFactory<V>): V where V : ContentTree<V> {
        if (!fs.isDirectory(path)) {
            return factory.empty
        }

        var loadCount = 0
        var tree: V? = null
        fs.list(path).forEach f@{
            if (!fs.isDirectory(it)) {
                return@f
            }

            val pack = loadPack(it, fs, factory) ?: return@f
            tree = tree?.merge(pack.tree) ?: pack.tree

            loadCount++
        }

        logger.info { "Loaded $loadCount content packs" }
        return tree ?: factory.empty
    }

    fun <V> loadPack(
        path: Path,
        fs: FileSystem,
        factory: ContentTreeFactory<V>
    ): ContentPack<V>? where V : ContentTree<V> {
        if (!fs.isDirectory(path)) {
            throw IllegalArgumentException("'$path' is not a directory.")
        }

        val packMeta = loadPackMeta(path, fs) ?: return null
        if (packMeta.dataVersion != factory.dataVersion) {
            logger.warn { "Skipping '${path.name}'; pack data version is ${packMeta.dataVersion} but only ${factory.dataVersion} is supported" }
            return null
        }

        val dataPath = path.resolve(PACK_DATA_DIR)
        val tree: V
        if (!fs.isDirectory(dataPath)) {
            logger.warn { "Empty pack '${path.name}': data path is not a directory" }
            tree = factory.empty
        } else {
            tree = factory.loadDir(dataPath, fs)
        }

        return ContentPack(packMeta, tree)
    }

    private fun loadPackMeta(packPath: Path, fs: FileSystem): ContentPackMeta? {
        val packMeta = packPath.resolve(PACK_META_FILE)
        if (!fs.isRegularFile(packMeta)) {
            logger.warn { "Pack directory $packMeta has no metadata file" }
            return null
        }

        val tomlFile = toml.read(packMeta, fs)
        when (val tomlResult = ContentPackMeta.CODEC.decode(TomlTranscoder.INSTANCE, tomlFile)) {
            is MResult.Error -> {
                logger.warn { "Failed to decode pack metadata: ${tomlResult.message}" }
                return null
            }

            is MResult.Ok -> return tomlResult.value
        }
    }
}