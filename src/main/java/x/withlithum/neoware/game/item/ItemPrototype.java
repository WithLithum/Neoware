package x.withlithum.neoware.game.item;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.minestom.server.item.ItemStack;

public interface ItemPrototype {
    ItemStack createItem(Key key);
}
