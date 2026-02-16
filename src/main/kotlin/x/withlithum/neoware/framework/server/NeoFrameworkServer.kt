/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.framework.server

import com.google.common.base.Stopwatch
import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import okio.Path
import x.withlithum.neoware.level.instances.InstanceCapsule
import x.withlithum.neoware.server.Bootstrap
import x.withlithum.neoware.server.commands.Commands
import x.withlithum.neoware.server.config.ServerListenOptions
import x.withlithum.neoware.server.player.PlayerBlocklist
import x.withlithum.neoware.server.player.PlayerBlocklistImpl
import x.withlithum.neoware.server.player.PlayerManager
import x.withlithum.neoware.server.player.PlayerRecorder
import java.util.concurrent.TimeUnit

abstract class NeoFrameworkServer {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    abstract val recorder: PlayerRecorder
    abstract val playerManager: PlayerManager

    private val listenOptions: ServerListenOptions
    private val mcServer: MinecraftServer

    val instance: InstanceCapsule by lazy { createInstance() }

    val banManager: PlayerBlocklist

    val basePath: Path

    var isRunning: Boolean = false
        private set

    constructor(listen: ServerListenOptions,
                basePath: Path) {

        this.listenOptions = listen
        mcServer = MinecraftServer.init(Auth.Online())

        this.basePath = basePath
        banManager = PlayerBlocklistImpl(basePath.resolve("ban.json").toNioPath())
    }

    /**
     * Instantiates a new [InstanceCapsule].
     */
    protected abstract fun createInstance(): InstanceCapsule

    /**
     * Executes implementation specific initialization actions. This method is called by [start]
     * prior to server startup.
     */
    protected abstract fun bootstrap()

    fun start() {
        val sw = Stopwatch.createStarted()

        // Load integrated services
        Bootstrap.bootstrap()
        banManager.load()
        Commands.register(this)

        bootstrap()

        // Create event node for integrated services
        val eventManager = MinecraftServer.getGlobalEventHandler()
        eventManager.addChild(playerManager.createEventNode())

        sw.stop()

        logger.info { "Setup took ${sw.elapsed(TimeUnit.MILLISECONDS)} ms" }
        mcServer.start(listenOptions.address, listenOptions.port)
        isRunning = true
        logger.info { "Started server at ${listenOptions.address}:${listenOptions.port}" }
    }

    fun stop() {
        banManager.save()
        recorder.save()

        if (!MinecraftServer.isStopping()) {
            MinecraftServer.stopCleanly()
        }

        isRunning = false
    }
}