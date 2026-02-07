/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io

import okio.FileSystem
import okio.Path

/**
 * Returns whether the specified path exists and is a directory.
 */
fun FileSystem.isDirectory(path: Path): Boolean {
    val meta = this.metadataOrNull(path)
    return meta != null && meta.isDirectory
}

/**
 * Returns whether the specified path exists and is a regular file.
 */
fun FileSystem.isRegularFile(path: Path): Boolean {
    val meta = this.metadataOrNull(path)
    return meta != null && meta.isRegularFile
}