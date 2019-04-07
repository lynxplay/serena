package me.ravvenlord.serena.listener;

import me.ravvenlord.serena.cooldown.CooldownContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final CooldownContainer cooldownContainer;

    public PlayerConnectionListener(CooldownContainer cooldownContainer) {
        this.cooldownContainer = cooldownContainer;
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        this.cooldownContainer.remove(event.getPlayer().getUniqueId());
    }

}
