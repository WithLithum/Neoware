/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.packs;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

/**
 * Declares a content pack.
 * @param name The name of the content pack to display.
 * @param dataVersion The data version of the content pack.
 */
public record ContentPackMeta(String name,
                              int dataVersion) {
    public static final Codec<ContentPackMeta> CODEC = StructCodec.struct(
        "name", Codec.STRING, ContentPackMeta::name,
        "data_version", Codec.INT, ContentPackMeta::dataVersion,
        ContentPackMeta::new
    );
}
