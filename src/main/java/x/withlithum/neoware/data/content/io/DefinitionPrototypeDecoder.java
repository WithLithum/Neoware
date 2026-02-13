/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.data.content.io;

import x.withlithum.neoware.data.game.ItemDefinition;
import x.withlithum.neoware.game.item.DefinitionPrototype;
import x.withlithum.neoware.game.item.ItemPrototype;
import x.withlithum.neoware.util.results.NeoResult;

import java.io.InputStream;

public final class DefinitionPrototypeDecoder implements ContentDecoder<ItemPrototype> {
    private final ContentDecoder<ItemDefinition> baseDecoder;

    public DefinitionPrototypeDecoder(ContentDecoder<ItemDefinition> baseDecoder) {
        this.baseDecoder = baseDecoder;
    }

    @Override
    public String getAcceptedExtension() {
        return baseDecoder.getAcceptedExtension();
    }

    @Override
    public NeoResult<ItemPrototype> load(InputStream stream) {
        return baseDecoder.load(stream).map(x ->
            NeoResult.ok(new DefinitionPrototype(x)));
    }
}
