/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import net.kyori.adventure.translation.GlobalTranslator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.data.locale.ResourceLocaleLoader;
import x.withlithum.neoware.game.locale.MapTranslator;
import x.withlithum.neoware.instance.behaviour.BehaviourManager;
import x.withlithum.neoware.instance.behaviour.BlockBehaviours;
import x.withlithum.neoware.level.block.NeonBlockHandlers;
import x.withlithum.neoware.server.commands.CommandFramework;

public final class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private Bootstrap() {
        throw new AssertionError("No Bootstrap instances for you!");
    }

    public static void bootstrap() {
        LOGGER.info("Initializing server environment");
        // Registries
        NeonBlockHandlers.register();

        // Locale
        GlobalTranslator.translator().addSource(new MapTranslator(
            ResourceLocaleLoader.loadFromResources(Bootstrap.class.getClassLoader())
        ));
        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;

        // commands
        CommandFramework.INSTANCE.initialize();

        BlockBehaviours.INSTANCE.addDefault();

        // events
        var eventSource = MinecraftServer.getGlobalEventHandler();
        eventSource.addChild(BehaviourManager.INSTANCE.createEventNode());
    }
}
