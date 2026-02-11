/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.instance

import net.minestom.server.instance.Instance

/**
 * Defines a type that encapsulates and manages an [Instance].
 */
interface ManagedInstance {
    val instance: Instance
}