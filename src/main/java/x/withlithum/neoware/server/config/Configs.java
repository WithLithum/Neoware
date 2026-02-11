package x.withlithum.neoware.server.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.framework.server.ServerListenEndpoint;

@NullMarked
public final class Configs {
    @Nullable
    private static Config CONFIG;

    public static final String KEY_LEVEL = "neoware.level";

    @Deprecated(forRemoval = true)
    public static final String KEY_LOBBY_LEVEL = "neoware.lobby.level";

    public static final String KEY_SERVER_ADDRESS = "neoware.server.address";
    public static final String KEY_SERVER_PORT = "neoware.server.port";

    public static Config get() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet loaded");
        }

        return CONFIG;
    }

    public static boolean has(String key) {
        return get().hasPath(key);
    }

    public static String getString(String key) {
        final var config = get();

        return config.getString(key);
    }

    public static ServerListenEndpoint getEndpoint() {
        final var config = get();

        final var address = config.getString(KEY_SERVER_ADDRESS);
        final var port = config.getInt(KEY_SERVER_PORT);

        return new ServerListenEndpoint(address, port);
    }

    public static void initialize() {
        CONFIG = ConfigFactory.load();
    }
}
