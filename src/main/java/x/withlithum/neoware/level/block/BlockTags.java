package x.withlithum.neoware.level.block;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.RegistryTag;

/**
 * Contains static references to built-in Minecraft block tags.
 *
 * @see <a href="https://minecraft.wiki/w/Block_tag_(Java_Edition)">"Block tag (Java Edition)"</a> on Minecraft Wiki
 */
public final class BlockTags {
    private BlockTags() {
        throw new AssertionError("No BlockTags instances for you!");
    }

    /**
     * The {@code minecraft:doors} tag, which contains all door blocks.
     */
    public static final RegistryTag<Block> DOORS = get("doors");

    @SuppressWarnings("SameParameterValue")
    private static RegistryTag<Block> get(@KeyPattern.Value String name) {
        return Block.staticRegistry().getTag(Key.key(Key.MINECRAFT_NAMESPACE, name));
    }
}
