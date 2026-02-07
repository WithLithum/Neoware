package x.withlithum.neoware.game.inventory;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.game.ItemRef;
import x.withlithum.neoware.game.item.ItemPrototypes;
import x.withlithum.neoware.server.NeoWareServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class InventoryResolver {
    private InventoryResolver() {}

    public static @Nullable ItemStack recoverReference(ItemRef ref) {
        var item = ItemPrototypes.createItem(ref.base());
        if (item == null) {
            return null;
        }

        if (ref.damage() != 0) {
            item = item.damage(ref.damage());
        }

        return item.withAmount(ref.count());
    }

    /**
     * Creates a reference from the specified stack.
     * @param stack The stack to create reference from.
     * @return The item reference, or {@code null} if {@code stack} is not a valid reference or is {@link ItemStack#AIR}.
     */
    public static @Nullable ItemRef createReference(ItemStack stack) {
        if (stack.isAir()) {
            return null;
        }

        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            log.warn("Item with material '{}' is not a reference", stack.material().key());
            return null;
        }

        var baseTag = customData.nbt().get("base");
        if (!(baseTag instanceof StringBinaryTag str)) {
            return null;
        }

        if (!Key.parseable(str.value())) {
            log.warn("Unparsable base ID: {}", str.value());
        }

        final var prototypeMap = NeoWareServer.INSTANCE.getContents().getItems();

        @SuppressWarnings("PatternValidation") var baseKey = Key.key(str.value());
        if (!prototypeMap.containsKey(baseKey)) {
            log.warn("Nonexistent base ID '{}' for an existing reference",
                baseKey);
        }

        // Get damage
        var damage = 0;
        var damValue = stack.get(DataComponents.DAMAGE);
        if (damValue != null) {
            damage = damValue;
        }

        return new ItemRef(baseKey,
            stack.amount(),
            damage);
    }

    public static @Unmodifiable List<ItemRef> resolve(AbstractInventory inventory) {
        final var itemStacks = inventory.getItemStacks();
        final var itemRefs = new ArrayList<ItemRef>(itemStacks.length);

        for (int i = 0; i < inventory.getSize(); i++) {
            var item = inventory.getItemStack(i);
            var ref = createReference(item);
            if (ref == null) {
                continue;
            }

            itemRefs.set(i, ref);
        }

        return Collections.unmodifiableList(itemRefs);
    }

    public static void recover(List<ItemRef> refs,
                               AbstractInventory inventory) {
        inventory.clear();
        for (int i = 0; i < refs.size(); i++) {
            var ref = refs.get(i);
            var stack = recoverReference(ref);
            if (stack == null) {
                continue;
            }

            inventory.setItemStack(i, stack);
        }
    }
}
