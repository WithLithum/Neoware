/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.persistence;

/**
 * Defines a service that needs loading data from an external source.
 */
public interface Loadable {
    /**
     * Loads external data.
     */
    void load();
}
