/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util

import net.minestom.server.coordinate.Pos
import x.withlithum.neoware.data.storage.Vector2F

fun castRotation(pos: Pos): Vector2F {
    return Vector2F(pos.yaw, pos.pitch)
}