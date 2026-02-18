/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.game;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.Material;
import net.minestom.server.item.enchant.Enchantment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class ItemDefinitionTest {
    @BeforeAll
    static void setup() {
        MinecraftServer.init();
    }

    @Test
    void testMaterial() {
        // Arrange
        final var definition = new ItemDefinition(Material.ACACIA_BOAT,
            null, null, null, null, null);

        // Act
        final var result = definition.createItem(Key.key("test"));

        // Assert
        assertEquals(Material.ACACIA_BOAT, result.material());
    }

    @Test
    void testName() {
        // Arrange
        final var definition = new ItemDefinition(Material.STONE,
            Component.text("This is a name!"),
            null, null, null, null);

        // Act
        final var result = definition.createItem(Key.key("test"));

        // Assert
        assertEquals(Component.text("This is a name!"), result.get(DataComponents.ITEM_NAME));
    }

    @Test
    void testLore() {
        // Arrange
        final var definition = new ItemDefinition(Material.BARRIER,
            null, List.of(Component.text("Hi")),
            null, null, null);

        // Act
        final var result = definition.createItem(Key.key("test"));

        // Assert
        final var lore = result.get(DataComponents.LORE);
        assertIterableEquals(List.of(Component.text("Hi")), lore);
    }

    @Test
    void testEnchantments() {
        // Arrange
        final var definition = new ItemDefinition(Material.STONE,
            null, null,
            Map.of(Enchantment.DENSITY.key(), 2,
                Enchantment.UNBREAKING.key(), 3),
            null,
            null);

        // Act
        final var result = definition.createItem(Key.key("test"));

        // Assert
        final var enchants = result.get(DataComponents.ENCHANTMENTS);
        assertNotNull(enchants);
        assertAll(() -> assertEquals(2, enchants.level(Enchantment.DENSITY)),
            () -> assertEquals(3, enchants.level(Enchantment.UNBREAKING)));
    }
}
