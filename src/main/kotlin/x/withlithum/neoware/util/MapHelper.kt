/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util

object MapHelper {
    /**
     * Merges two map instances. The second map will overwrite the first map.
     */
    fun <K, V> mergeMaps(map1: Map<K, V>, map2: Map<K, V>): Map<K, V> {
        val result = mutableMapOf<K, V>().apply { putAll(map1) }
        map2.forEach {
            result[it.key] = it.value
        }
        return result
    }
}