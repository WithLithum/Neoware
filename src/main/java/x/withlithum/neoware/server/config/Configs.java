package x.withlithum.neoware.server.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jspecify.annotations.Nullable;

public final class Configs {
    @Nullable
    private static Config CONFIG;

    public static final String KEY_WORLD_SEED = "neoware.world.seed";

    public static final String KEY_LOBBY_LEVEL = "neoware.lobby.level";

    public static final String KEY_SERVER_ADDRESS = "neoware.server.address";
    public static final String KEY_SERVER_PORT = "neoware.server.port";

    public static Config get() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet loaded");
        }

        return CONFIG;
    }

    public static void initialize() {
        CONFIG = ConfigFactory.load();
    }
}
