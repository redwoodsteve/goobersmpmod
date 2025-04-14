package com.redwoodsteve.goobersmpmod.config;

import com.google.gson.Gson;
import com.redwoodsteve.goobersmpmod.Goobersmpmod;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Config {
    public static Path configPath = FabricLoader.getInstance().getConfigDir().resolve("goobersmpmod_saveData.json");
    static Gson gson = new Gson();
    private static final Logger logger = Goobersmpmod.logger;

    /**
     * Sets up the config. If there is no config file, or if <code>forceCreate</code> is enabled, it will make a config file.
     * When the config file is made, it sets the config to the defaults. The defaults are:
     * <li>maceCraftable: <code>true</code></li>
     * @param forceCreate If true, always create/re-create the config file
     * @return If the file was created successfully, it will return true, if not, return false
     */
    public static boolean setupConfig(boolean forceCreate) {
        if (Files.exists(configPath)) {
            return true;
        } else if (!Files.exists(configPath) || forceCreate) {
            try {
                Files.writeString(configPath, gson.toJson(new ConfigContent(true)), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                return true;
            } catch (Throwable e) {
                logger.error("Error when creating file: \"" + e.getMessage() + "\"");
                return false;
            }
        }
        return false;
    }

    /**
     * Resets the config file with the given options. Will reset the config file.
     * @param content The config content to put in the file
     * @return True if operation successful, false if otherwise
     */
    public static boolean setConfig(ConfigContent content) {
        try {
            Files.writeString(configPath, gson.toJson(content), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (Throwable e) {
            logger.error("Error when modifying file: \"{}\"", e.getMessage());
            return false;
        }
    }

    /**
     * Gets the contents of the config file.
     * @return The contents of the config file, as a <code>ConfigContent</code> class.
     */
    public static ConfigContent getConfig() {
        try {
            return gson.fromJson(Files.readString(configPath), ConfigContent.class);
        } catch (Throwable e) {
            logger.error("Error reading config: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Sets the specified value of the config to something else.
     * @param key The config field to change (an invalid option causes issues)
     * @param value The value that the field will be set to
     * @return True if operation successful, false if otherwise
     */
    public static boolean changeConfig(ConfigField key, Object value) {
        ConfigContent data = getConfig();
        if (data == null) {
            logger.error("An unexpected error occurred when updating config");
            return false;
        }

        Method m;
        ConfigContent changedConfig;
        try {

            m = data.getClass().getMethod(key.name() + "Set", Object.class);

            changedConfig = (ConfigContent) m.invoke(data, value);

        } catch (Exception e) {
            logger.error("Error in changeConfig when trying to access method \"{}\": {}", key.name(), e.toString());
            return false;
        }

        return setConfig(changedConfig);

    }
}