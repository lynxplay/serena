package dev.lynxplay.serena.listener;

import dev.lynxplay.serena.FixedScheduler;
import dev.lynxplay.serena.configuration.properties.PropertyConfiguration;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record PlayerThrowListener(
    PropertyConfiguration propertyConfiguration,
    FixedScheduler scheduler
) implements Listener {

    @EventHandler
    public void onEntityThrow(final PrePlayerAttackEntityEvent event) {
        final Player player = event.getPlayer();
        final List<Entity> passengers = new ArrayList<>(player.getPassengers());
        final Set<EntityType> bannedEntityTypes = this.propertyConfiguration.bannedEntityTypes();
        passengers.removeIf(e -> bannedEntityTypes.contains(e.getType()));

        if (passengers.isEmpty()) return;

        event.setCancelled(true);

        passengers.forEach(player::removePassenger);
        scheduler.runTaskDelayed(() -> passengers.forEach(e -> e.setVelocity(player.getLocation()
                .getDirection()
                .multiply(this.propertyConfiguration.throwVelocityMultiplier())
                .add(player.getVelocity())))
            , 1L);
    }

}
