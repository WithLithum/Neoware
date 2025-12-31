package x.withlithum.neoware.level.worldgen.generators;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;

public final class LobbyPlaceholderGenerator implements Generator {
    @Override
    public void generate(GenerationUnit unit) {
        unit.modifier().fillHeight(-63, -62, Block.BARRIER);
    }
}
