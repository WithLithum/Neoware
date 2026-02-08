package x.withlithum.neoware.game.item;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.server.NeoWareServer;

import java.util.*;

@Slf4j
@NullMarked
public final class ItemPrototypes {
    private static final Map<Key, ItemStack> cache = new HashMap<>();

    public static @Nullable ItemStack createItem(Key key) {
        return cache.computeIfAbsent(key, ItemPrototypes::computeItem);
    }

    private static @Nullable ItemStack computeItem(Key key) {
        var prototype = NeoWareServer.INSTANCE.getContents().getItems().get(key);
        if (prototype == null) {
            return null;
        }

        return prototype.createItem(key);
    }
}
