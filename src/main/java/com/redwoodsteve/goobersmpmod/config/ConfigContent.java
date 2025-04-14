package com.redwoodsteve.goobersmpmod.config;

/**
 * Represents the contents of the config file.
 * @param maceCraftable If the mace should be craftable or not.
 */
public record ConfigContent(boolean maceCraftable) {
    public ConfigContent maceCraftableSet(Object v) {
        boolean b = (boolean) v;
        return new ConfigContent(b);
    }
}
