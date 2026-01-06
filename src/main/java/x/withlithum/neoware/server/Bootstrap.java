/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.server;

import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.kyori.adventure.translation.GlobalTranslator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.game.item.ItemPrototypes;
import x.withlithum.neoware.game.locale.LocaleLoader;
import x.withlithum.neoware.instance.behaviour.BehaviourManager;
import x.withlithum.neoware.instance.behaviour.BlockBehaviours;
import x.withlithum.neoware.level.block.NeonBlockHandlers;
import x.withlithum.neoware.level.security.LevelSecurityAgent;
import x.withlithum.neoware.server.commands.CommandFramework;
import x.withlithum.neoware.server.commands.Commands;

public final class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private Bootstrap() {
        throw new AssertionError("No Bootstrap instances for you!");
    }

    public static void bootstrap() {
        LOGGER.info("Initializing server environment");
        // Registries
        NeonBlockHandlers.register();
        ItemPrototypes.initialize();

        // Locale
        GlobalTranslator.translator().addSource(LocaleLoader.loadEmbedded(Bootstrap.class.getClassLoader()));
        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;

        // commands
        CommandFramework.INSTANCE.initialize();
        Commands.register();

        // security
        NeoWareServer.INSTANCE.banManager.loadList();

        // gameplay
        MinestomPvP.init();
        var modernVanilla = CombatFeatures.modernVanilla();

        BlockBehaviours.INSTANCE.addDefault();

        // events
        var eventSource = MinecraftServer.getGlobalEventHandler();
        eventSource.addChild(modernVanilla.createNode());
        eventSource.addChild(NeoWareServer.INSTANCE.playerManager.createEventNode());
        eventSource.addChild(LevelSecurityAgent.createEventNode());
        eventSource.addChild(BehaviourManager.INSTANCE.createEventNode());
    }
}
