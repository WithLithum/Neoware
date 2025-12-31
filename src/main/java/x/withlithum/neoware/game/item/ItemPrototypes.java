package x.withlithum.neoware.game.item;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.game.DefinitionLoader;

import java.io.*;
import java.util.*;

@Slf4j
@NullMarked
public final class ItemPrototypes {
    public static final @Unmodifiable Map<Key, ItemPrototype> ITEMS = DefinitionLoader.loadItems();
    private static final Map<Key, ItemStack> cache = new HashMap<>();

    public static @Nullable ItemStack createItem(Key key) {
        return cache.computeIfAbsent(key, ItemPrototypes::computeItem);
    }

    private static @Nullable ItemStack computeItem(Key key) {
        var prototype = ITEMS.get(key);
        if (prototype == null) {
            return null;
        }

        return prototype.createItem();
    }

    public static void initialize() {
        log.info("initialize()");
    }
}
