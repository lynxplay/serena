package dev.lynxplay.serena.listener;

import dev.lynxplay.serena.FixedScheduler;
import dev.lynxplay.serena.configuration.properties.PropertyConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        final List<Entity> passengers = new ArrayList<>(player.getPassengers());
        final Set<EntityType> bannedEntityTypes = this.propertyConfiguration.bannedEntityTypes();
        passengers.removeIf(e -> bannedEntityTypes.contains(e.getType()));

        if (passengers.isEmpty()) return;

        passengers.forEach(player::removePassenger);
        scheduler.runTaskDelayed(() -> passengers.forEach(e -> e.setVelocity(player.getLocation()
                .getDirection()
                .multiply(this.propertyConfiguration.throwVelocityMultiplier())
                .add(player.getVelocity())))
            , 1L);
    }

}
