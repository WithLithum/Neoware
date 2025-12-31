package x.withlithum.neoware.experiment;

import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.value.TomlValue;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.codec.Result;
import net.minestom.server.item.Material;
import x.withlithum.neoware.data.encode.TomlTranscoder;
import x.withlithum.neoware.data.game.ItemDefinition;
import x.withlithum.neoware.util.KeyRoot;

import java.util.List;

@Slf4j
class EncodeOptionalExperiment implements CliExperiment {
    @Override
    public void execute() {
        final var itemDefinition = new ItemDefinition(Material.STONE,
            null,
            List.of(),
            null,
            null,
            null);

        var result = ItemDefinition.CODEC.encode(TomlTranscoder.INSTANCE,
            itemDefinition);
        switch (result) {
            case Result.Ok<TomlValue>(TomlValue value) -> log.info(JToml.jToml().writeToString(value.asTable()));
            case Result.Error<TomlValue>(String message) -> log.error(message);
        }
    }
}
