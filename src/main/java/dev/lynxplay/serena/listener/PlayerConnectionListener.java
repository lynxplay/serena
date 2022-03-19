package dev.lynxplay.serena.listener;

import dev.lynxplay.serena.cooldown.CooldownContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final CooldownContainer cooldownContainer;

    public PlayerConnectionListener(final CooldownContainer cooldownContainer) {
        this.cooldownContainer = cooldownContainer;
    }

    @EventHandler
    public void onDisconnect(final PlayerQuitEvent event) {
        this.cooldownContainer.remove(event.getPlayer().getUniqueId());
    }

}
