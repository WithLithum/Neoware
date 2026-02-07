/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs

import x.withlithum.neoware.data.content.hierarchy.ContentTree

data class ContentPack(val meta: ContentPackMeta,
    val tree: ContentTree)
