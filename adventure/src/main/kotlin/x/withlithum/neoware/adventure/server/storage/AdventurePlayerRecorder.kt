/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.server.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.minestom.server.entity.Player
import okio.FileSystem
import okio.IOException
import okio.Path
import okio.buffer
import x.withlithum.neoware.adventure.content.item.AdventureItemManager
import x.withlithum.neoware.data.encode.KJsonTranscoder
import x.withlithum.neoware.data.player.PlayerInfo
import x.withlithum.neoware.data.storage.PlayerRecorder
import x.withlithum.neoware.data.storage.Savable
import x.withlithum.neoware.util.io.isDirectory
import x.withlithum.neoware.util.io.isRegularFile
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AdventurePlayerRecorder(
    private val itemManager: AdventureItemManager,
    private val storagePath: Path,
    private val storageFs: FileSystem
) : PlayerRecorder,
    Savable {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val staging = ConcurrentHashMap<UUID, PlayerInfo>()

    override fun capturePlayer(player: Player) {
        staging[player.uuid] = PlayerDataUtil.storePlayer(player, itemManager)
    }

    override fun preRewindPlayer(player: Player) {
        if (staging.containsKey(player.uuid)) {
            return
        }

        val data = load(player.uuid)
        if (data != null) {
            staging[player.uuid] = data
        }
    }

    override fun rewindPlayer(player: Player) {
        val cached = staging[player.uuid]
        if (cached != null) {
            PlayerDataUtil.recoverPlayer(player, cached, itemManager)
            return
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun load(uuid: UUID): PlayerInfo? {
        val path = storagePath.resolve("${uuid}.dat")
        if (!storageFs.isRegularFile(path)) {
            return null
        }

        try {
            return storageFs.openReadOnly(path).use { file ->
                file.source().buffer().inputStream().use { stream ->
                    Json.decodeFromStream<PlayerInfo>(stream)
                }
            }
        } catch (e: Exception) {
            logger.warn(e) { "Could not load player with UUID '$uuid'" }
            return null
        }
    }

    override fun save() {
        if (storageFs.exists(storagePath) && !storageFs.isDirectory(storagePath)) {
            throw IllegalStateException("Storage path '$storagePath' has something other than a directory")
        }

        storageFs.createDirectory(storagePath)

        for (entry in staging.entries) {
            val path = storagePath.resolve("${entry.key}.dat")
            savePlayer(entry.value, path)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun savePlayer(info: PlayerInfo, path: Path) {
        val value = PlayerInfo.CODEC.encode(KJsonTranscoder, info)

        try {
            storageFs.openReadWrite(path).use {
                it.sink().buffer().outputStream().use {
                    Json.encodeToStream(value, it)
                }
            }
        } catch (e: IOException) {
            logger.warn(e) { "Cannot save player to path '$path'" }
        }
    }
}