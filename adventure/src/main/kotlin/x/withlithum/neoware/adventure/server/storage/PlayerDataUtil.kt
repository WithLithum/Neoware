/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.server.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.entity.Player
import net.minestom.server.inventory.AbstractInventory
import net.minestom.server.item.ItemStack
import x.withlithum.neoware.adventure.content.item.AdventureItemManager
import x.withlithum.neoware.data.game.ItemRef
import x.withlithum.neoware.data.player.PlayerInfo
import x.withlithum.neoware.data.player.PlayerStatus
import x.withlithum.neoware.server.commands.PermissionRank
import x.withlithum.neoware.util.results.NeoResult

object PlayerDataUtil {
    const val VERSION = 3

    private val logger = KotlinLogging.logger {}

    fun recoverInventory(refs: List<ItemRef>,
                         inventory: AbstractInventory,
                         itemManager: AdventureItemManager) {
        inventory.clear()

        for (i in refs.indices) {
            val ref = refs[i]
            when (val result = itemManager.resolveRef(ref)) {
                is NeoResult.Error -> result.logWarn(logger)
                is NeoResult.Ok -> inventory.setItemStack(i, result.value, false)
            }
            inventory.update()
        }
    }

    fun recoverPlayer(player: Player, playerInfo: PlayerInfo, itemManager: AdventureItemManager) {
        player.permissionLevel = playerInfo.rank.ordinal
        playerInfo.status.apply(player)

        if (playerInfo.items != null) {
            val items = playerInfo.items!!
            if (player.inventory.size != items.size) {
                logger.warn {"Inventory size mismatch (player ${player.inventory.size} vs data ${items.size})" }
            } else {
                recoverInventory(items, player.inventory, itemManager)
            }
        }
    }

    fun storeInventory(inventory: AbstractInventory, itemManager: AdventureItemManager): List<ItemRef> {
        val contents = Array(inventory.size) { ItemStack.AIR }
        inventory.copyContents(contents)
        val refs = ArrayList<ItemRef>(contents.size)

        for (i in contents.indices) {
            val item = contents[i]
            when (val result = itemManager.createRef(item)) {
                is NeoResult.Error -> {
                    result.logWarn(logger)
                    refs.add(ItemRef.AIR)
                }
                is NeoResult.Ok -> refs.add(result.value)
            }
        }

        return refs
    }

    fun storePlayer(player: Player, itemManager: AdventureItemManager): PlayerInfo {
        val inventory = storeInventory(player.inventory, itemManager)

        val permission = if (player.permissionLevel > 4) {
            PermissionRank.WHEEL
        } else {
            PermissionRank.entries[player.permissionLevel]
        }

        return PlayerInfo(VERSION,
            permission,
            PlayerStatus.create(player),
            inventory)
    }
}