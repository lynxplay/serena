package me.ravvenlord.serena.listener;

import me.ravvenlord.serena.configuration.language.LanguageConfiguration;
import me.ravvenlord.serena.configuration.properties.PropertyConfiguration;
import me.ravvenlord.serena.cooldown.CooldownContainer;
import me.ravvenlord.serena.permission.PlayerPermissionChecker;
import me.ravvenlord.serena.player.PlayerToggleRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.Duration;
import java.util.function.Function;

public class PlayerPickupListener implements Listener {

    private final CooldownContainer cooldownContainer;
    private final LanguageConfiguration languageConfiguration;
    private final PlayerToggleRegistry toggleRegistry;
    private final PlayerPermissionChecker permissionChecker;
    private final PropertyConfiguration propertyConfiguration;
    private final Function<Player, String> nameLookup;

    public PlayerPickupListener(CooldownContainer cooldownContainer
            , LanguageConfiguration languageConfiguration
            , PlayerToggleRegistry toggleRegistry
            , PlayerPermissionChecker permissionChecker
            , PropertyConfiguration propertyConfiguration
            , Function<Player, String> nameLookup) {
        this.cooldownContainer = cooldownContainer;
        this.languageConfiguration = languageConfiguration;
        this.toggleRegistry = toggleRegistry;
        this.permissionChecker = permissionChecker;
        this.propertyConfiguration = propertyConfiguration;
        this.nameLookup = nameLookup;
    }

    @EventHandler
    public void onPickup(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Entity target = event.getRightClicked();
        if (!(target instanceof LivingEntity)) return;

        Duration durationLeft = this.cooldownContainer.getCooldownLeft(player.getUniqueId()).orElse(null);
        if (durationLeft != null) {
            player.sendMessage(this.languageConfiguration.playerPickupCooldown(durationLeft));
            return;
        }

        if (!(target instanceof Player)) {
            if (!this.permissionChecker.pickupEntity(player, target)) {
                player.sendMessage(this.languageConfiguration.permissionMissing());
                return;
            }
        } else {
            Player targetPlayer = (Player) target;
            if (!this.permissionChecker.pickupPlayer(player, targetPlayer)) {
                player.sendMessage(this.languageConfiguration.permissionMissing());
                return;
            }

            if (!this.toggleRegistry.getCurrentToggle(target.getUniqueId())
                    && !this.permissionChecker.bypassToggle(player)) {
                player.sendMessage(this.languageConfiguration.playerCannotBePickedUp(this.nameLookup.apply(targetPlayer)));
                return;
            }
        }

        if (player.getPassengers().contains(target)) return;

        player.addPassenger(target);
        this.cooldownContainer.startCooldown(player.getUniqueId(), propertyConfiguration.playerPickupCooldown());
    }

}
