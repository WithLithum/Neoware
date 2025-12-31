package x.withlithum.neoware.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Namespaced;
import org.jetbrains.annotations.NotNull;

public final class KeyRoot implements Namespaced {
    public static final KeyRoot instance = new KeyRoot();

    private KeyRoot() {
    }

    public static Key id(@KeyPattern.Value String value) {
        return Key.key(instance, value);
    }

    @KeyPattern.Namespace
    @Override
    public @NotNull String namespace() {
        return "neon";
    }
}
