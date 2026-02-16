/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance.behaviour

import net.minestom.server.coordinate.BlockVec
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerHand
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace

@Deprecated("Use BlockBehaviour java interface instead.")
fun interface BlockBehaviour {
    fun apply(block: Block,
              pos: BlockVec,
              face: BlockFace,
              instance: Instance,
              hand: PlayerHand,
              player: Player): BlockUseAction
}