package x.withlithum.neoware.data.encode;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MiniMessageCodec implements Codec<Component> {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final MiniMessageCodec INSTANCE = new MiniMessageCodec();

    private MiniMessageCodec() {}

    @Override
    public <D> Result<Component> decode(Transcoder<D> coder, D value) {
        var str = coder.getString(value);
        return str.mapResult(miniMessage::deserialize);
    }

    @Override
    public <D> Result<D> encode(Transcoder<D> coder, @Nullable Component value) {
        if (value == null) {
            return new Result.Ok<>(coder.createNull());
        }

        return new Result.Ok<>(coder.createString(miniMessage.serialize(value)));
    }
}
