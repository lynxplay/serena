package me.ravvenlord.serena.configuration.language;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ImmutableLanguageConfiguration implements LanguageConfiguration {

    private String prefix;
    private String playerPickupCooldown;
    private String pickupDisabled;
    private String pickupEnabled;
    private String permissionMissing;
    private String playerCannotBePickedUp;
    private String reloadComplete;

    public ImmutableLanguageConfiguration(String prefix
            , String playerPickupCooldown
            , String pickupDisabled
            , String pickupEnabled
            , String permissionMissing
            , String playerCannotBePickedUp, String reloadComplete) {
        this.prefix = prefix;
        this.playerPickupCooldown = playerPickupCooldown;
        this.pickupDisabled = pickupDisabled;
        this.pickupEnabled = pickupEnabled;
        this.permissionMissing = permissionMissing;
        this.playerCannotBePickedUp = playerCannotBePickedUp;
        this.reloadComplete = reloadComplete;
    }

    /**
     * Returns the raw plugin prefix
     *
     * @return the prefix configured in the language pack
     */
    @Override
    public String prefix() {
        return this.prefix;
    }

    /**
     * Returns the string to display that the pick up was unsuccessful due to the player's pick-up cooldown
     * still being on cooldown. This cooldown rests on the player that is trying to pick something up.
     *
     * @param timeLeft the time left before the entity can be picked up again
     * @return the message
     */
    @Override
    public String playerPickupCooldown(Duration timeLeft) {
        return prefix() + String.format(this.playerPickupCooldown, timeLeft.get(ChronoUnit.SECONDS));
    }

    /**
     * Returns the string send when a player cannot be picked up as he toggled it off
     *
     * @param playerName the name of the player who cannot be picked up
     * @return the player
     */
    @Override
    public String playerCannotBePickedUp(String playerName) {
        return prefix() + String.format(this.playerCannotBePickedUp, playerName);
    }

    /**
     * Returns the message send when a player disables the ability to be picked up
     *
     * @return the message
     */
    @Override
    public String pickupDisabled() {
        return prefix() + this.pickupDisabled;
    }

    /**
     * Returns the message send when a player enables the ability to be picked up
     *
     * @return the message send
     */
    @Override
    public String pickupEnabled() {
        return prefix() + this.pickupEnabled;
    }

    /**
     * Returns the message send to a player when he is missing the permission for an interaction with this plugin
     *
     * @return the message
     */
    @Override
    public String permissionMissing() {
        return prefix() + this.permissionMissing;
    }

    /**
     * Returns the message returned when the plugin was reloaded successfully
     *
     * @return the message
     */
    @Override
    public String reloadComplete() {
        return prefix() + this.reloadComplete;
    }
}
