package x.withlithum.neoware.experiment;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
@Slf4j
public final class Main {
    private static final @Unmodifiable Map<String, CliExperiment> EXPERIMENTS = Map.of(
    );

    static void main(String[] args) {
        if (args.length != 1) {
            log.error("Please specify the name of an experiment");
            System.exit(1);
        }

        final var exp = EXPERIMENTS.get(args[0]);
        if (exp == null) {
            log.error("No such experiment '{}'", args[0]);
            System.exit(1);
        }

        try {
            exp.execute();
        } catch (Exception e) {
            log.error("Failed to execute experiment", e);
            System.exit(1);
        }
    }
}
