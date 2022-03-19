package dev.lynxplay.serena.configuration.language;

import dev.lynxplay.serena.configuration.ConfigurationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.Configuration;

public class ImmutableLanguageConfigurationFactory implements ConfigurationFactory<LanguageConfiguration> {

    /**
     * Creates the instance of the configuration data object
     *
     * @param configuration the spigot configuration instance to base it on
     *
     * @return the created instance
     */
    @Override
    public LanguageConfiguration create(final Configuration configuration) {
        return new ImmutableLanguageConfiguration(
            parse(getString(configuration, "prefix")),
            getString(configuration, "player-pickup-cooldown"),
            parse(getString(configuration, "pickup-disabled")),
            parse(getString(configuration, "pickup-enabled")),
            parse(getString(configuration, "permission-missing")),
            getString(configuration, "player-cannot-be-picked-up"),
            parse(getString(configuration, "reload-complete"))
        );
    }

    /**
     * Parses a plain component using mini message.
     *
     * @param string the string base for the component in mm format
     *
     * @return the parsed component.
     */
    private Component parse(final String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    /**
     * Returns the string found in the config or a very basic default message based on the path
     *
     * @param configuration the configuration to read from
     * @param path          the path to lookup
     *
     * @return the string
     */
    private String getString(final Configuration configuration, final String path) {
        return configuration.getString(path, "{" + path + "}");
    }

}
