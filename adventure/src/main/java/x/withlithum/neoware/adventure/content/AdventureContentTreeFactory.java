/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.adventure.content;

import x.withlithum.neoware.data.content.hierarchy.ContentTreeFactory;
import x.withlithum.neoware.util.results.NeoResult;

import java.nio.file.Path;

public final class AdventureContentTreeFactory implements ContentTreeFactory<AdventureContentTree> {
    private AdventureContentTreeFactory() {
    }

    public static final AdventureContentTreeFactory INSTANCE = new AdventureContentTreeFactory();

    @Override
    public AdventureContentTree getEmpty() {
        return AdventureContentTree.EMPTY;
    }

    @Override
    public int getDataVersion() {
        return 1;
    }

    @Override
    public NeoResult<AdventureContentTree> loadDir(Path directory) {
        return AdventureContentTree.loadDir(directory);
    }
}
