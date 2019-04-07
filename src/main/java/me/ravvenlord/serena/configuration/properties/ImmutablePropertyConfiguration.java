package me.ravvenlord.serena.configuration.properties;

import java.time.Duration;

public class ImmutablePropertyConfiguration implements PropertyConfiguration {

    private final Duration playerPickupCooldown;
    private final double throwVelocityMultiplier;

    ImmutablePropertyConfiguration(Duration playerPickupCooldown, double throwVelocityMultiplier) {
        this.playerPickupCooldown = playerPickupCooldown;
        this.throwVelocityMultiplier = throwVelocityMultiplier;
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
}
