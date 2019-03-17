package me.ravvenlord.serena;

/**
 * The {@link FixedScheduler} interface defines a fixed spigot scheduler
 */
public interface FixedScheduler {

    /**
     * Runs the task after x spigot ticks
     *
     * @param task the task to execute
     * @param delay the delay in ticks
     */
    void runTaskDelayed(Runnable task, long delay);

}
