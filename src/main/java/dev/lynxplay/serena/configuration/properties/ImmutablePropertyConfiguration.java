package dev.lynxplay.serena.configuration.properties;

import org.bukkit.entity.EntityType;

import java.time.Duration;
import java.util.EnumSet;
import java.util.Set;

public class ImmutablePropertyConfiguration implements PropertyConfiguration {

    private final Duration playerPickupCooldown;
    private final double throwVelocityMultiplier;
    private final Set<EntityType> bannedEntityTypes;

    ImmutablePropertyConfiguration(Duration playerPickupCooldown, double throwVelocityMultiplier,
                                   Set<EntityType> bannedEntityTypes) {
        this.playerPickupCooldown = playerPickupCooldown;
        this.throwVelocityMultiplier = throwVelocityMultiplier;
        this.bannedEntityTypes = bannedEntityTypes;
    }

    /**
     * Returns the cooldown a player has between picking up entities
     *
     * @return the duration
     */
    @Override
    public Duration playerPickupCooldown() {
        return this.playerPickupCooldown;
    }

    /**
     * Returns the multiplier applied to the player velocity when throwing an entity
     *
     * @return the double value
     */
    @Override
    public double throwVelocityMultiplier() {
        return throwVelocityMultiplier;
    }

    /**
     * Provides a set containing all entity types that are banned from being picked up by serena.
     * An entity type that is listed in the set cannot be picked up or thrown off the player by serena.
     *
     * @return the set instance. The set is mutable but is not backed by this configuration, hence modifications to the
     * set will not modify this configuration instance.
     */
    @Override
    public Set<EntityType> bannedEntityTypes() {
        return EnumSet.copyOf(bannedEntityTypes);
    }

}
