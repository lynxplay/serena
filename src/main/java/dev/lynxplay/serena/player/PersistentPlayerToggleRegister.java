package dev.lynxplay.serena.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * The persistent player toggle register uses the {@link org.bukkit.persistence.PersistentDataHolder} interface provided
 * by spigot to store the current toggle value for a player.
 */
public class PersistentPlayerToggleRegister implements PlayerToggleRegistry {

    private final Function<UUID, Player> playerLookup;
    private final NamespacedKey toggleNamespace;

    public PersistentPlayerToggleRegister(final Function<UUID, Player> playerLookup,
                                          final NamespacedKey toggleNamespace) {
        this.playerLookup = playerLookup;
        this.toggleNamespace = toggleNamespace;
    }

    /**
     * Returns the current toggle value, being {@code true} if the toggle is enabled, else {@code false}
     *
     * @param uuid the uuid to check against
     *
     * @return the toggle value
     */
    @Override
    public boolean getCurrentToggle(UUID uuid) {
        return Optional.of(uuid)
            .map(this.playerLookup)
            .map(PersistentDataHolder::getPersistentDataContainer)
            .map(c -> c.get(this.toggleNamespace, PersistentDataType.BYTE))
            .map(b -> b == 1)
            .orElse(false);
    }

    /**
     * Sets the toggle value
     *
     * @param uuid  the uuid to update
     * @param value the new value for the given uuid
     *
     * @return the previous value
     */
    @Override
    public boolean setToggle(UUID uuid, boolean value) {
        return Optional.of(uuid)
            .map(this.playerLookup)
            .map(PersistentDataHolder::getPersistentDataContainer)
            .map(c -> {
                final boolean previousValue = Optional.ofNullable(c.get(this.toggleNamespace, PersistentDataType.BYTE))
                    .map(b -> b == 1)
                    .orElse(false);

                c.set(this.toggleNamespace, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
                return previousValue;
            })
            .orElse(false);
    }

}
