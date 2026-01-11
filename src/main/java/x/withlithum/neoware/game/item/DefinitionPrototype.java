package x.withlithum.neoware.game.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.data.game.ItemDefinition;

@NullMarked
public final class DefinitionPrototype implements ItemPrototype {
    private final ItemDefinition itemDefinition;

    public DefinitionPrototype(ItemDefinition itemDefinition) {
        this.itemDefinition = itemDefinition;
    }

    @Override
    public ItemStack createItem(Key key) {
        return itemDefinition.createItem(key);
    }
}
