/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.game

import net.minestom.server.component.DataComponents
import net.minestom.server.item.ItemStack

val ItemStack.damage: Int
    get() = this.get(DataComponents.DAMAGE) ?: 0