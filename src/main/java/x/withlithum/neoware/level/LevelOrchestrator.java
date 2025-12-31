package x.withlithum.neoware.level;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import x.withlithum.neoware.level.dimension.KnownInstance;
import x.withlithum.neoware.level.dimension.NeonDimensionTypes;
import x.withlithum.neoware.level.worldgen.generators.LobbyPlaceholderGenerator;
import x.withlithum.neoware.level.worldgen.generators.SimplexGenerator;
import x.withlithum.neoware.server.config.Configs;

@Slf4j
public final class LevelOrchestrator {
	public final InstanceContainer venturedPhasesLevel;
    public final InstanceContainer lobbyLevel;

	public LevelOrchestrator() {
		var instanceManager = MinecraftServer.getInstanceManager();
		
		venturedPhasesLevel = instanceManager.createInstanceContainer(NeonDimensionTypes.VENTURED_PHASES);
		venturedPhasesLevel.setChunkSupplier(LightingChunk::new);
		venturedPhasesLevel.setGenerator(new SimplexGenerator(Configs.get().getLong(Configs.KEY_WORLD_SEED)));

        lobbyLevel = instanceManager.createInstanceContainer(NeonDimensionTypes.LOBBY);
        lobbyLevel.setChunkSupplier(LightingChunk::new);
        if (Configs.get().hasPath(Configs.KEY_LOBBY_LEVEL)) {
            lobbyLevel.setChunkLoader(new AnvilLoader(Configs.get().getString(Configs.KEY_LOBBY_LEVEL)));
        } else {
            log.warn("Lobby level is not specified, will be using placeholder for the entire map!");
        }
        lobbyLevel.setGenerator(new LobbyPlaceholderGenerator());
	}

    public Instance getKnownInstance(KnownInstance knownInstance) {
        return switch (knownInstance) {
            case LOBBY -> lobbyLevel;
            case VENTURED_PHASES -> venturedPhasesLevel;
        };
    }

    public KnownInstance toKnownInstance(Instance instance) {
        if (instance == lobbyLevel) {
            return KnownInstance.LOBBY;
        } else if (instance ==  venturedPhasesLevel) {
            return KnownInstance.VENTURED_PHASES;
        } else {
            throw new IllegalStateException("Unknown instance: " + instance.getDimensionName());
        }
    }
}
