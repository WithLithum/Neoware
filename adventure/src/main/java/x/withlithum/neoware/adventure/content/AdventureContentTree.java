/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import x.withlithum.neoware.data.content.hierarchy.ContentTree;
import x.withlithum.neoware.data.content.io.ContentDecoder;
import x.withlithum.neoware.data.content.io.ContentIo;
import x.withlithum.neoware.data.content.io.DefinitionPrototypeDecoder;
import x.withlithum.neoware.data.content.io.GsonCodecContentDecoder;
import x.withlithum.neoware.data.game.ItemDefinition;
import x.withlithum.neoware.game.item.ItemPrototype;
import x.withlithum.neoware.util.MapHelper;
import x.withlithum.neoware.util.results.NeoResult;

import java.nio.file.Path;
import java.util.Map;

@NullMarked
public record AdventureContentTree(@Unmodifiable Map<Key, ItemPrototype> items)
    implements ContentTree<AdventureContentTree> {

    private static final ContentDecoder<ItemPrototype> ITEM_DECODER =
        new DefinitionPrototypeDecoder(new GsonCodecContentDecoder<>(ItemDefinition.CODEC));

    public static final AdventureContentTree EMPTY = new AdventureContentTree(Map.of());

    public static NeoResult<AdventureContentTree> loadDir(Path path) {
        return ContentIo.walkContent(path, "item", ITEM_DECODER)
            .map(items -> NeoResult.ok(new AdventureContentTree(items)));
    }

    @Override
    public AdventureContentTree merge(AdventureContentTree other) {
        return new AdventureContentTree(MapHelper.mergeMaps(items, other.items));
    }
}
