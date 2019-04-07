package me.ravvenlord.serena.listener;

import me.ravvenlord.serena.FixedScheduler;
import me.ravvenlord.serena.configuration.properties.PropertyConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class PlayerThrowListener implements Listener {

    private final PropertyConfiguration propertyConfiguration;
    private final FixedScheduler scheduler;

    public PlayerThrowListener(PropertyConfiguration propertyConfiguration, FixedScheduler scheduler) {
        this.propertyConfiguration = propertyConfiguration;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onEntityThrow(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        Player player = event.getPlayer();
        ArrayList<Entity> passengers = new ArrayList<>(player.getPassengers());

        if (passengers.isEmpty()) return;

        player.eject();
        scheduler.runTaskDelayed(() -> passengers.forEach(e -> e.setVelocity(player.getLocation()
                        .getDirection()
                        .multiply(this.propertyConfiguration.throwVelocityMultiplier())
                        .add(player.getVelocity())))
                , 1L);
    }
}
