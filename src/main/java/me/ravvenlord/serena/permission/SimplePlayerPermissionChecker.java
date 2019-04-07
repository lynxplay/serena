package me.ravvenlord.serena.permission;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public class SimplePlayerPermissionChecker implements PlayerPermissionChecker {
    /**
     * Returns if the permissible instance has the permission to use the serena toggle command
     *
     * @param permissible the permissible instance
     *
     * @return the result
     */
    @Override
    public boolean useSerenaToggleCommand(Permissible permissible) {
        return permissible.hasPermission("serena.toggle");
    }

    /**
     * Returns if the permissible can use the reload command
     *
     * @param permissible the permissible to check against
     *
     * @return the result
     */
    @Override
    public boolean useSerenaReloadCommand(Permissible permissible) {
        return permissible.hasPermission("serena.reload");
    }

    /**
     * Returns if the player has the permission to pickup the entity
     *
     * @param permissible the permissible to check against
     * @param entity the entity to pickup
     *
     * @return the result
     */
    @Override
    public boolean pickupEntity(Permissible permissible, Entity entity) {
        return permissible.hasPermission("serena.pickup.entity");
    }

    /**
     * Returns if the player has the permission to pickup the entity
     *
     * @param permissible the permissible to check against
     * @param entity the entity to pickup
     *
     * @return the result
     */
    @Override
    public boolean pickupPlayer(Permissible permissible, Player entity) {
        return permissible.hasPermission("serena.pickup.player");
    }

    /**
     * Returns if the permissible instance can bypass a toggled decision
     *
     * @param permissible the permissible instance
     *
     * @return the result
     */
    @Override
    public boolean bypassToggle(Permissible permissible) {
        return permissible.hasPermission("serena.pickup.bypass");
    }
}
