package com.redwoodsteve.goobersmpmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**The main class of the mod. This is where events and other things get registered.*/
public class Goobersmpmod implements ModInitializer {
    /**The modid of the mod*/
    public static final String MOD_ID = "goobersmpmod";
    /**The Log4j logger*/
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);

    /**Code to run when the mod is initialized */
    @Override
    public void onInitialize() {
        logger.info("hi goobers");

        ServerLifecycleEvents.SERVER_STARTED.register(EventListeners::onServerStart);

    }
}
