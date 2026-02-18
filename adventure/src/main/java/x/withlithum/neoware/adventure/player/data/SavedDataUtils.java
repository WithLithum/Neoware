/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.player.data;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.adventure.content.item.AdventureItemManager;
import x.withlithum.neoware.data.game.ItemRef;
import x.withlithum.neoware.data.player.PlayerInfo;
import x.withlithum.neoware.data.player.PlayerStatus;
import x.withlithum.neoware.server.commands.PermissionRank;
import x.withlithum.neoware.util.results.NeoResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NullMarked
public final class SavedDataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SavedDataUtils.class);
    private static final int DATA_VERSION = 1;

    private SavedDataUtils() {
        throw new AssertionError("No PlayerDataUtils instances for you!");
    }

    /**
     * Applies the list of references to the specified inventory. All items in the list will be put
     * into the inventory with the same index.
     * <p>
     *     There exists a corner case where the size of the specified refs list may differ to the
     *     size of the list. This may happen due to a mistake or due to a change made by Mojang. If
     *     this were to happen:
     * </p>
     * <ul>
     *     <li>
     *         If the ref list is <i>smaller</i> than inventory, the remaining slots will be
     *         empty.
     *     </li>
     *     <li>
     *         If the ref list is <i>bigger</i>, the remaining items are <b>not</b> applied, and a
     *         log message will be produced.
     *     </li>
     * </ul>
     *
     * @param refs The refs to apply.
     * @param inventory The inventory to apply to.
     * @param itemManager The item manager to use to recover refs from.
     * @return Whether there are any items remaining that were not applied.
     */
    public static boolean applyInventory(List<ItemRef> refs,
                                      AbstractInventory inventory,
                                      AdventureItemManager itemManager) {
        inventory.clear();

        final var size = refs.size();
        final var targetSize = inventory.getSize();
        var thrownAway = false;

        for (int i = 0; i < size; i++) {
            if (i >= targetSize) {
                LOGGER.warn("Inventory size is smaller than the number of refs, not restoring remaining items");
                thrownAway = true;
                break;
            }

            final var ref = refs.get(i);
            final var itemResult = itemManager.resolveRef(ref);
            switch (itemResult) {
                case NeoResult.Ok<ItemStack> ok -> inventory.setItemStack(i, ok.getValue(), false);
                case NeoResult.Error<ItemStack> error -> {
                    error.logWarn(LOGGER);
                    inventory.setItemStack(i, ItemStack.AIR, false);
                }
            }
        }

        inventory.update();
        return !thrownAway;
    }

    /**
     * Applies the specified {@link PlayerInfo} onto the specified {@link Player}.
     * <p>
     * The restoration of inventory contents involves a potential corner case that is gracefully
     * handled but may result in data loss. See
     * {@link #applyInventory(List, AbstractInventory, AdventureItemManager)}.
     *
     * @param player The player to apply player info to.
     * @param info The info to apply.
     * @param itemManager The item manager used to resolve refs.
     * @return Whether there are any items remaining that were not applied.
     */
    public static boolean applyPlayer(Player player,
                                   PlayerInfo info,
                                   AdventureItemManager itemManager) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(info);
        Objects.requireNonNull(itemManager);

        player.setPermissionLevel(info.rank().ordinal());
        info.status().apply(player);

        if (info.items() != null) {
            return applyInventory(info.items(), player.getInventory(), itemManager);
        }
        return true;
    }

    public static @Unmodifiable List<ItemRef> recordInventory(AbstractInventory inventory,
                                                              AdventureItemManager itemManager) {
        final var contents = new ItemStack[inventory.getSize()];
        Arrays.fill(contents, ItemStack.AIR);
        inventory.copyContents(contents);

        final var refs = new ItemRef[contents.length];
        for (int i = 0; i < contents.length; i++) {
            final var item = contents[i];
            final var refResult = itemManager.createRef(item);

            switch (refResult) {
                case NeoResult.Ok<ItemRef> ok -> refs[i] = ok.getValue();
                case NeoResult.Error<ItemRef> error -> {
                    error.logWarn(LOGGER);
                    refs[i] = ItemRef.AIR;
                }
            }
        }

        return List.of(refs);
    }

    public static PlayerInfo recordPlayer(Player player,
                                          AdventureItemManager itemManager) {
        final var inventory = recordInventory(player.getInventory(), itemManager);
        final var permissionLevel = player.getPermissionLevel();
        final var rank = permissionLevel > 4 ? PermissionRank.WHEEL : PermissionRank.values()[permissionLevel];

        return new PlayerInfo(DATA_VERSION, rank,
            PlayerStatus.create(player),
            inventory);
    }
}
