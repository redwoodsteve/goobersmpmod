package com.redwoodsteve.goobersmpmod;

import com.redwoodsteve.goobersmpmod.config.Config;
import com.redwoodsteve.goobersmpmod.config.ConfigContent;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

/**The methods to run when events happen.*/
public class EventListeners {

    private static final Logger logger = Goobersmpmod.logger;
    public static MinecraftServer server = null;
    /**The event that runs when the server starts up.*/
    public static void onServerStart(MinecraftServer fserver) {
        logger.debug("Server started");
        server = fserver;

        Config.setupConfig(false);
        //Config.setConfig(new ConfigContent(false));

    }
}
