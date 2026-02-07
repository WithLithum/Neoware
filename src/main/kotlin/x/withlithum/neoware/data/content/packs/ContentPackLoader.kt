/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.wasabithumb.jtoml.JToml
import okio.FileSystem
import okio.Path
import x.withlithum.neoware.data.content.OkFileSystemContentSource
import x.withlithum.neoware.data.content.hierarchy.ContentTree
import x.withlithum.neoware.data.encode.TomlTranscoder
import x.withlithum.neoware.server.NeoWareServer
import x.withlithum.neoware.util.io.isDirectory
import x.withlithum.neoware.util.io.isRegularFile
import x.withlithum.neoware.util.io.read
import net.minestom.server.codec.Result as MResult

object ContentPackLoader {
    const val PACK_META_FILE = "neoware_pack.toml"
    const val PACK_DATA_DIR = "data"

    private val logger = KotlinLogging.logger {}
    private val toml = JToml.jToml()

    fun loadPack(path: Path, fs: FileSystem): ContentPack? {
        if (!fs.isDirectory(path)) {
            throw IllegalArgumentException("'$path' is not a directory.")
        }

        val packMeta = loadPackMeta(path, fs) ?: return null
        if (packMeta.dataVersion != NeoWareServer.PACK_DATA_VERSION) {
            logger.warn { "Skipping '${path.name}'; pack data version is ${packMeta.dataVersion} but ${NeoWareServer.PACK_DATA_VERSION} is supported" }
            return null
        }

        val dataPath = path.resolve(PACK_DATA_DIR)
        val tree: ContentTree
        if (!fs.isDirectory(dataPath)) {
            logger.warn { "Empty pack '${path.name}': data path is not a directory" }
            tree = ContentTree.EMPTY
        } else {
            tree = ContentTree.load(OkFileSystemContentSource(dataPath, fs))
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