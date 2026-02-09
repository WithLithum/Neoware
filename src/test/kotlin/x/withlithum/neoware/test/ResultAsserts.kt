/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.test

import net.minestom.server.codec.Result
import kotlin.test.asserter

fun <V> assertIsOk(result: Result<V>): V {
    if (result is Result.Error) {
        asserter.fail("Codec failed: ${result.message}")
    }

    return result.orElseThrow()
}