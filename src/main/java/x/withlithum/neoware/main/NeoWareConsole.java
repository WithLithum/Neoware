package x.withlithum.neoware.main;

import net.minecrell.terminalconsole.SimpleTerminalConsole;
import net.minestom.server.MinecraftServer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;
import x.withlithum.neoware.server.NeoWareServer;

import java.io.IOException;

public class NeoWareConsole extends SimpleTerminalConsole {
    @Override
    protected boolean isRunning() {
        return NeoWareServer.INSTANCE.isRunning();
    }

    @Override
    protected void runCommand(String s) {
        MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(),
            s);
    }

    @Override
    protected void shutdown() {
        NeoWareServer.INSTANCE.stop();
    }
}
