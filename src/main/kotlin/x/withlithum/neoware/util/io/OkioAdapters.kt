/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io

import io.github.wasabithumb.jtoml.JToml
import io.github.wasabithumb.jtoml.document.TomlDocument
import okio.FileSystem
import okio.Path
import okio.buffer

fun JToml.read(path: Path, fs: FileSystem): TomlDocument {
    fs.openReadOnly(path).use {
        val source = it.source().buffer()
        return this.read(source.inputStream())
    }
}