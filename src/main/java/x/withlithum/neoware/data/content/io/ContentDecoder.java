/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import x.withlithum.neoware.util.results.NeoResult;

import java.io.InputStream;

/// Decodes the specified content from its encoded from.
///
/// @param <V> The type of the content to load.
public interface ContentDecoder<V> {
    /// Returns the file extension name accepted by this instance.
    String getAcceptedExtension();

    /// Decodes the encoded content from the specified stream.
    ///
    /// The implementations of this method does not guarantee the stream will be closed. The caller
    /// is responsible for ensuring resource cleanup.
    ///
    /// @param stream The stream to decode.
    /// @return The result of the decode operation.
    NeoResult<V> load(InputStream stream);
}
