/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.level.instances;

import net.minestom.server.instance.Instance;
import org.jspecify.annotations.NullMarked;

/**
 * Defines a service that manages an encapsulated instance.
 */
@NullMarked
public interface InstanceCapsule {
    /**
     * Gets the encapsulated instance.
     * @return The encapsulated instance.
     */
    Instance instance();
}
