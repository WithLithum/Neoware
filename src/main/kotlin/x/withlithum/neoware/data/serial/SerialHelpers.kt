/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.serial

import kotlinx.serialization.SerializationException
import net.kyori.adventure.key.Key

fun serialCheckKeyParsable(string: String) {
    if (!Key.parseable(string)) {
        throw SerializationException("Invalid identifier '$string'")
    }
}