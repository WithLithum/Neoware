/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.block.behaviour;

import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.instance.behaviour.BlockUseAction;

@FunctionalInterface
@NullMarked
public interface BlockBehaviour {
    BlockUseAction apply(BlockInteractionInfo interaction);
}
