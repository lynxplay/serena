package dev.lynxplay.serena.configuration.properties;

import java.time.Duration;

/**
 * The {@link PropertyConfiguration} defines a configuration that contains the properties of this plugin
 */
public interface PropertyConfiguration {

    /**
     * Returns the cooldown a player has between picking up entities
     *
     * @return the duration
     */
    Duration playerPickupCooldown();

    /**
     * Returns the multiplier applied to the player velocity when throwing an entity
     *
     * @return the double value
     */
    double throwVelocityMultiplier();
}
