package me.ravvenlord.serena.player;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuePlayerToggleRegistry implements PlayerToggleRegistry {

    private final Queue<UUID> playerQueue = new ConcurrentLinkedQueue<>();

    /**
     * Returns the current toggle value, being {@code true} if the toggle is enabled, else {@code false}
     *
     * @param uuid the uuid to check against
     *
     * @return the toggle value
     */
    @Override
    public boolean getCurrentToggle(UUID uuid) {
        return this.playerQueue.contains(uuid);
    }

    /**
     * Sets the toggle value
     *
     * @param uuid the uuid to update
     * @param value the new value for the given uuid
     *
     * @return the previous value
     */
    @Override
    public boolean setToggle(UUID uuid, boolean value) {
        boolean current = getCurrentToggle(uuid);

        if (value && !current) {
            this.playerQueue.add(uuid);
        } else if (!value && current) {
            this.playerQueue.remove(uuid);
        }
        return current;
    }
}
