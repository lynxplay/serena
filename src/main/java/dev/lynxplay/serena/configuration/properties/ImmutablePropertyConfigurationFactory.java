package dev.lynxplay.serena.configuration.properties;

import dev.lynxplay.serena.configuration.ConfigurationFactory;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ImmutablePropertyConfigurationFactory implements ConfigurationFactory<PropertyConfiguration> {

    /**
     * Creates the instance of the configuration data object
     *
     * @param configuration the spigot configuration instance to base it on
     *
     * @return the created instance
     */
    @Override
    public PropertyConfiguration create(final Configuration configuration) {
        final Map<String, EntityType> keyList = Arrays.stream(EntityType.values())
            .filter(t -> t != EntityType.UNKNOWN)
            .collect(Collectors.toMap(t -> t.getKey().getKey(), Function.identity()));
        final EnumSet<EntityType> bannedEntityTypes = configuration.getStringList("banned-entity-types").stream()
            .map(keyList::get)
            .filter(Objects::nonNull)
            .collect(() -> EnumSet.noneOf(EntityType.class), EnumSet::add, EnumSet::addAll);

        return new ImmutablePropertyConfiguration(
            Duration.of(configuration.getLong("player-pickup-cooldown", 10L), ChronoUnit.SECONDS),
            configuration.getDouble("player-throw-multiplier", 1),
            bannedEntityTypes
        );
    }

}
