package x.withlithum.neoware.server.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jspecify.annotations.Nullable;

public final class Configs {
    @Nullable
    private static Config CONFIG;

    public static final String KEY_WORLD_SEED = "neoware.world.seed";

    public static final String KEY_LOBBY_LEVEL = "neoware.lobby.level";

    public static final String KEY_DATABASE_LOCATION = "neoware.database.location";
    public static final String KEY_DATABASE_USER = "neoware.database.user";
    public static final String KEY_DATABASE_PASSWORD = "neoware.database.password";

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
