package x.withlithum.neoware.main;

import net.minecrell.terminalconsole.SimpleTerminalConsole;
import net.minestom.server.MinecraftServer;
import x.withlithum.neoware.framework.server.NeoFrameworkServer;

public class NeoWareConsole extends SimpleTerminalConsole {
    private final NeoFrameworkServer server;

    public NeoWareConsole(NeoFrameworkServer server) {
        this.server = server;
    }

    @Override
    protected boolean isRunning() {
        return server.isRunning();
    }

    @Override
    protected void runCommand(String s) {
        MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(),
            s);
    }

    @Override
    protected void shutdown() {
        server.stop();
    }
}
