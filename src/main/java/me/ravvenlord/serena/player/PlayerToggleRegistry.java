package me.ravvenlord.serena.player;

import java.util.UUID;

/**
 * The player toggle registry interface defines an object that can store a toggle value
 */
public interface PlayerToggleRegistry {

    /**
     * Returns the current toggle value, being {@code true} if the toggle is enabled, else {@code false}
     *
     * @param uuid the uuid to check against
     *
     * @return the toggle value
     */
    boolean getCurrentToggle(UUID uuid);

    /**
     * Sets the toggle value
     *
     * @param uuid the uuid to update
     * @param value the new value for the given uuid
     *
     * @return the previous value
     */
    boolean setToggle(UUID uuid, boolean value);

    /**
     * Toggles the current value of the player, negating the current value
     *
     * @param uuid the uuid to update
     *
     * @return the new toggle value
     */
    default boolean toggle(UUID uuid) {
        boolean newValue = !this.getCurrentToggle(uuid);
        this.setToggle(uuid, newValue);
        return newValue;
    }
}
