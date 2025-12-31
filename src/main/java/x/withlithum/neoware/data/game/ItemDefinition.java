package x.withlithum.neoware.data.game;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.RegistryKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import x.withlithum.neoware.data.encode.MiniMessageCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@NullMarked
public record ItemDefinition(Material material,
                             @Nullable Component name,
                             @Nullable List<Component> lore,
                             @Nullable Map<Key, Integer> enchantments,
                             @Nullable AttributeList attributeModifiers,
                             @Nullable Set<DataComponent<?>> hideTooltips) {
    public static final Codec<ItemDefinition> CODEC = StructCodec.struct(
        "material", Material.CODEC, ItemDefinition::material,
        "name", MiniMessageCodec.INSTANCE.optional(), ItemDefinition::name,
        "lore", MiniMessageCodec.INSTANCE.list(256).optional(), ItemDefinition::lore,
        "enchantments", Codec.KEY.mapValue(Codec.INT), ItemDefinition::enchantments,
        "attribute_modifiers", AttributeList.CODEC.optional(), ItemDefinition::attributeModifiers,
        "hide_tooltips", DataComponent.CODEC.set().optional(), ItemDefinition::hideTooltips,
        ItemDefinition::new
    );

    public ItemStack createItem(Key id) {
        var builder = ItemStack.builder(material);
        builder.set(DataComponents.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder()
            .put("base", StringBinaryTag.stringBinaryTag(id.asString()))
            .build()));
        if (name != null) {
            builder.set(DataComponents.ITEM_NAME, name);
        }
        if (lore != null) {
            builder.lore(lore);
        }
        if (enchantments != null) {
            builder.set(DataComponents.ENCHANTMENTS, createEnchantmentList(id, enchantments));
        }
        if (attributeModifiers != null) {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attributeModifiers);
        }
        if (hideTooltips != null) {
            builder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false,
                hideTooltips));
        }
        return builder.build();
    }

    private static EnchantmentList createEnchantmentList(Key defId,
                                                         Map<Key, Integer> enchantments) {
        var el = new HashMap<RegistryKey<Enchantment>, Integer>(enchantments.size());
        var registry = MinecraftServer.getEnchantmentRegistry();

        for (var enchantId : enchantments.keySet()) {
            var regKey = registry.getKey(enchantments.get(enchantId));
            if (regKey == null) {
                log.warn("Definition '{}' has non-existent enchantment '{}' declared",
                    defId,
                    enchantId);
                continue;
            }

            el.put(regKey, enchantments.get(enchantId));
        }

        return new EnchantmentList(el);
    }
}
