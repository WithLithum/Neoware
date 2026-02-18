/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content.item;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.adventure.content.AdventureContentTree;
import x.withlithum.neoware.data.game.ItemRef;
import x.withlithum.neoware.util.results.NeoResult;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public class AdventureItemManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdventureItemManager.class);

    private final AdventureContentTree contentTree;
    private final Map<Key, ItemStack> cache = new HashMap<>();

    public AdventureItemManager(AdventureContentTree contentTree) {
        this.contentTree = contentTree;
    }

    private static ItemStack missingItemPlaceholder(Key key) {
        return ItemStack.builder(Material.BARRIER)
            .customName(
                Component.text(
                    "!!! MISSING PROTOTYPE !!!",
                    NamedTextColor.RED, TextDecoration.BOLD, TextDecoration.UNDERLINED
                )
            )
            .lore(Component.text(key.asString(), NamedTextColor.GRAY))
            .build();
    }

    /**
     * Gets an item stack for the specified base ID.
     * <p>
     * For this method, {@code neoware:air} is a special reference key that always results in
     * {@link ItemStack#AIR} being created, regardless of whether that key exists in the content
     * tree.
     *
     * @param key The base ID.
     * @return
     * The created item, or {@link ItemStack#AIR} if the base ID is {@code neoware:air} or was not
     * found.
     */
    public ItemStack getItem(Key key) {
        if (key == ItemRef.AIR_KEY) {
            return ItemStack.AIR;
        }

        return cache.computeIfAbsent(key, this::computeItem);
    }

    private ItemStack computeItem(Key key) {
        final var prototype = contentTree.items().get(key);
        if (prototype == null) {
            LOGGER.warn("Unknown item prototype '{}'", key.asString());
            return missingItemPlaceholder(key);
        }

        return prototype.createItem(key);
    }

    /**
     * Resolves the specified [ItemRef] into a new [ItemStack]. Returns a failure result only if
     * the base ID cannot be resolved.
     *
     * @param ref The reference to resolve.
     * @return The result of the resolution.
     */
    public NeoResult<ItemStack> resolveRef(ItemRef ref) {
        if (ref.isAir()) {
            return NeoResult.ok(ItemStack.AIR);
        }

        var item = getItem(ref.base());
        if (ref.damage() != 0) {
            item = item.damage(ref.damage());
        }
        if (ref.count() > 1) {
            item = item.withAmount(ref.count());
        }

        return NeoResult.ok(item);
    }

    /**
     * Creates a new instance of [ItemRef] from the specified [ItemStack].
     */
    @SuppressWarnings("PatternValidation")
    public NeoResult<ItemRef> createRef(ItemStack item) {
        if (item.isAir()) {
            return NeoResult.ok(ItemRef.AIR);
        }

        final var customData = item.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return NeoResult.error("Missing custom data");
        }
        final var baseTag = customData.nbt().get("base");

        if (!(baseTag instanceof StringBinaryTag baseStr) || !Key.parseable(baseStr.value())) {
            return NeoResult.error("Missing or invalid base ID");
        }

        final var baseKey = Key.key(baseStr.value());
        if (!contentTree.items().containsKey(baseKey)) {
            return NeoResult.error("Unrecognised prototype ID '${baseKey.value()}'");
        }

        return NeoResult.ok(
            new ItemRef(
                baseKey,
                item.amount(),
                item.get(DataComponents.DAMAGE, 0)
            )
        );
    }
}
