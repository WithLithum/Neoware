package x.withlithum.neoware.game.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.data.game.ItemDefinition;

@NullMarked
public final class DefinitionPrototype implements ItemPrototype {
    private final Key key;
    private final ItemDefinition itemDefinition;

    public DefinitionPrototype(Key key, ItemDefinition itemDefinition) {
        this.key = key;
        this.itemDefinition = itemDefinition;
    }

    @Override
    public ItemStack createItem() {
        return itemDefinition.createItem(key);
    }

    @Override
    public Key key() {
        return key;
    }
}
