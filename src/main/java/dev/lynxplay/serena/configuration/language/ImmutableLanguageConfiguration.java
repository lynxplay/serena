package dev.lynxplay.serena.configuration.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.time.Duration;

public record ImmutableLanguageConfiguration(Component prefix,
                                             String playerPickupCooldown,
                                             Component pickupDisabled,
                                             Component pickupEnabled,
                                             Component permissionMissing,
                                             String playerCannotBePickedUp,
                                             Component reloadComplete) implements LanguageConfiguration {

    /**
     * Returns the raw plugin prefix
     *
     * @return the prefix configured in the language pack
     */
    @Override
    public Component prefix() {
        return this.prefix;
    }

    /**
     * Returns the string to display that the pick up was unsuccessful due to the player's pick-up cooldown still being
     * on cooldown. This cooldown rests on the player that is trying to pick something up.
     *
     * @param timeLeft the time left before the entity can be picked up again
     *
     * @return the message
     */
    @Override
    public Component playerPickupCooldown(final Duration timeLeft) {
        return prefix().append(
            MiniMessage.miniMessage().deserialize(this.playerPickupCooldown, TagResolver.resolver(
                Placeholder.component("duration", Component.text(timeLeft.getSeconds()))
            ))
        );
    }

    /**
     * Returns the string send when a player cannot be picked up as he toggled it off
     *
     * @param playerName the name of the player who cannot be picked up
     *
     * @return the player
     */
    @Override
    public Component playerCannotBePickedUp(final Component playerName) {
        return prefix().append(
            MiniMessage.miniMessage().deserialize(this.playerCannotBePickedUp, TagResolver.resolver(
                Placeholder.component("player", playerName)
            ))
        );
    }

    /**
     * Returns the message send when a player disables the ability to be picked up
     *
     * @return the message
     */
    @Override
    public Component pickupDisabled() {
        return prefix().append(this.pickupDisabled);
    }

    /**
     * Returns the message send when a player enables the ability to be picked up
     *
     * @return the message send
     */
    @Override
    public Component pickupEnabled() {
        return prefix().append(this.pickupEnabled);
    }

    /**
     * Returns the message send to a player when he is missing the permission for an interaction with this plugin
     *
     * @return the message
     */
    @Override
    public Component permissionMissing() {
        return prefix().append(this.permissionMissing);
    }

    /**
     * Returns the message returned when the plugin was reloaded successfully
     *
     * @return the message
     */
    @Override
    public Component reloadComplete() {
        return prefix().append(this.reloadComplete);
    }

}
