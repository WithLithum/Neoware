/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server.security

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.codec.Codec
import net.minestom.server.codec.Result
import net.minestom.server.codec.Transcoder
import x.withlithum.neoware.data.encode.StringUuidCodec
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.LinkedList
import java.util.UUID
import kotlin.math.exp
import kotlin.time.Clock
import kotlin.time.Instant

class BanManagerImpl(val banFile: Path) : BanManager {
    companion object {
        private val GSON = Gson()
        private val LOG = KotlinLogging.logger {}
        private val LIST_CODEC = StringUuidCodec.mapValue(BanInfo.CODEC)
        private const val BAN_LIST_CLEANING_THRESHOLD = 15

        private fun saveListInternal(file: Path, data: JsonElement) {
            try {
                OutputStreamWriter(
                    Files.newOutputStream(
                        file,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING
                    )
                ).use {
                    GSON.toJson(data, it)
                }
            } catch (e: IOException) {
                LOG.warn(e) { "Unable to save ban list" }
            }
        }
    }

    private var banList = mutableMapOf<UUID, BanInfo>()

    /**
     * Makes an attempt to load the ban list file specified in [banFile] property. If there is an
     * error loading the list, reports an error in the log.
     */
    override fun loadList() {
        if (!Files.exists(banFile)) {
            return
        }

        val element: JsonElement
        try {
            element = JsonReader(InputStreamReader(Files.newInputStream(banFile))).use {
                JsonParser.parseReader(it)
            }
        } catch (e: IOException) {
            LOG.warn(e) { "Unable to load ban list" }
            return
        }

        when (val r = LIST_CODEC.decode(Transcoder.JSON, element)) {
            is Result.Ok -> { banList = HashMap(r.value) }
            is Result.Error -> LOG.warn { "Unable to load decode list: ${r.message}" }
        }
    }

    override fun isBanned(uuid: UUID): Boolean {
        val banEntry = banList[uuid] ?: return false

        return banEntry.to == null || Clock.System.now() < banEntry.to
    }

    override fun getInfo(uuid: UUID): BanInfo? {
        return banList[uuid]
    }

    override fun ban(uuid: UUID, reason: String?, until: Instant?): BanInfo {
        val now = Clock.System.now()
        if (until != null && now >= until) {
            throw IllegalArgumentException("Ban expiration time must be later than the current time.")
        }

        val result = BanInfo(reason = reason,
            from = now,
            to = until)
        banList[uuid] = result
        return result
    }

    override fun remove(uuid: UUID) {
        if (banList.remove(uuid) == null) {
            throw IllegalArgumentException("The UUID '$uuid' is not on the ban list.")
        }
    }

    /**
     * Makes an attempt to save the ban list to the file specified in [banFile] property. If there
     * is an error saving the list, reports an error in the log.
     */
    override fun saveList() {
        if (banList.size > BAN_LIST_CLEANING_THRESHOLD) {
            removeExpired()
        }

        when (val r = LIST_CODEC.encode(Transcoder.JSON, banList)) {
            is Result.Ok -> saveListInternal(banFile, r.value)
            is Result.Error -> LOG.warn { "Unable to encode ban list: ${r.message}" }
        }
    }

    private fun removeExpired() {
        if (banList.isEmpty()) {
            return
        }

        val expiredList = LinkedList<UUID>()
        val now = Clock.System.now()

        for (info in banList) {
            val to = info.value.to
            if (to != null && now >= to) {
                expiredList.add(info.key)
            }
        }

        for (expiry in expiredList) {
            banList.remove(expiry)
        }
    }
}