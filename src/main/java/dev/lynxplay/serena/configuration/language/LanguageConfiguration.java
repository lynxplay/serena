package dev.lynxplay.serena.configuration.language;

import net.kyori.adventure.text.Component;

import java.time.Duration;

/**
 * The {@link LanguageConfiguration} interface defines a configuration that provides the language output of the plugin
 */
public interface LanguageConfiguration {

    /**
     * Returns the raw plugin prefix
     *
     * @return the prefix configured in the language pack
     */
    Component prefix();

    /**
     * Returns the component to display that the pick up was unsuccessful due to the player's pick-up cooldown still being
     * on cooldown. This cooldown rests on the player that is trying to pick something up.
     *
     * @param timeLeft the time left before the entity can be picked up again
     *
     * @return the message
     */
    Component playerPickupCooldown(Duration timeLeft);

    /**
     * Returns the component send when a player cannot be picked up as he toggled it off
     *
     * @param playerName the name of the player who cannot be picked up
     *
     * @return the player
     */
    Component playerCannotBePickedUp(Component playerName);

    /**
     * Returns the message send when a player disables the ability to be picked up
     *
     * @return the message
     */
    Component pickupDisabled();

    /**
     * Returns the message send when a player enables the ability to be picked up
     *
     * @return the message send
     */
    Component pickupEnabled();

    /**
     * Returns the message send to a player when he is missing the permission for an interaction with this plugin
     *
     * @return the message
     */
    Component permissionMissing();

    /**
     * Returns the message returned when the plugin was reloaded successfully
     *
     * @return the message
     */
    Component reloadComplete();

}
