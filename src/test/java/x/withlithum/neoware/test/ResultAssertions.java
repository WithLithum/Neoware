package x.withlithum.neoware.test;

import net.minestom.server.codec.Result;
import org.junit.jupiter.api.Assertions;

public final class ResultAssertions {
    public static <V> V assertResultOk(Result<V> result) {
        if (result instanceof Result.Error<V>(String message)) {
            Assertions.fail(message);
        }

        return ((Result.Ok<V>)result).value();
    }

    public static <V> void assertResultError(Result<V> result) {
        if (result instanceof Result.Ok<V>) {
            Assertions.fail("Result is successful but error is expected");
        }
    }
}
