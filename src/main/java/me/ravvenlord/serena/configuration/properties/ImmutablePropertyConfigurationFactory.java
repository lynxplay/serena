package me.ravvenlord.serena.configuration.properties;

import me.ravvenlord.serena.configuration.ConfigurationFactory;
import org.bukkit.configuration.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ImmutablePropertyConfigurationFactory implements ConfigurationFactory<PropertyConfiguration> {
    /**
     * Creates the instance of the configuration data object
     *
     * @param configuration the spigot configuration instance to base it on
     *
     * @return the created instance
     */
    @Override
    public PropertyConfiguration create(Configuration configuration) {
        return new ImmutablePropertyConfiguration(
                Duration.of(configuration.getLong("player-pickup-cooldown", 10L), ChronoUnit.SECONDS),
                configuration.getDouble("player-throw-multiplier", 1)
        );
    }
}
