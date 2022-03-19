package dev.lynxplay.serena.commands;

import dev.lynxplay.serena.Reloadable;
import dev.lynxplay.serena.configuration.language.LanguageConfiguration;
import dev.lynxplay.serena.permission.PlayerPermissionChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record SerenaReloadCommand(PlayerPermissionChecker permissionChecker,
                                  LanguageConfiguration languageConfiguration,
                                  Reloadable reloadable
) implements CommandExecutor {

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the
     * player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     *
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(final @NotNull CommandSender sender,
                             final @NotNull Command command,
                             final @NotNull String label,
                             final String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage(this.languageConfiguration.prefix() + "Only players can use this command");
            return true;
        }

        if (!permissionChecker.useSerenaReloadCommand(player)) {
            player.sendMessage(this.languageConfiguration.permissionMissing());
            return true;
        }

        this.reloadable.reload();
        player.sendMessage(this.languageConfiguration.reloadComplete());
        return true;
    }

}
