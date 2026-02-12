package x.withlithum.neoware.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x.withlithum.neoware.server.NeoWareServer;
import x.withlithum.neoware.server.config.ServerSettings;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * Defines the entry point of the NeoWare application.
	 */
	static void main() {
        LOGGER.info("Starting NeoWare");

//        // Console thread setup
//        var console = new NeoWareConsole();
//        var consoleThread = new Thread(console::start);
//        consoleThread.setDaemon(true);
//        consoleThread.setName("Console thread");

        // Start server
        ServerSettings.init();
		NeoWareServer.INSTANCE.start();
//        consoleThread.start();
	}

}
