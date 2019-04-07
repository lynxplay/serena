package me.ravvenlord.serena.configuration;

import org.bukkit.configuration.Configuration;

/**
 * The {@link ConfigurationFactory} interface describes an object that can manufacture a configuration data object from
 * a
 *
 * @param <T>
 */
public interface ConfigurationFactory<T> {

    /**
     * Creates the instance of the configuration data object
     *
     * @param configuration the spigot configuration instance to base it on
     *
     * @return the created instance
     */
    T create(Configuration configuration);

}
