package me.ravvenlord.serena.cooldown;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface CooldownContainer {

    /**
     * Starts a cooldown for the given uuid
     *
     * @param uuid the uuid
     * @param duration the duration this cooldown will last
     */
    void startCooldown(UUID uuid, Duration duration);

    /**
     * Returns the duration the cooldown for the uuid will last
     *
     * @param uuid the uuid to check for
     *
     * @return the duration wrapped in an {@link Optional}
     */
    Optional<Duration> getCooldownLeft(UUID uuid);

    /**
     * Removes the uuids cooldown set from the container if no longer valid
     *
     * @param uuid the uuid
     */
    void remove(UUID uuid);
}
