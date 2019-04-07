package me.ravvenlord.serena.commands;

import me.ravvenlord.serena.Reloadable;
import me.ravvenlord.serena.configuration.language.LanguageConfiguration;
import me.ravvenlord.serena.permission.PlayerPermissionChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SerenaReloadCommand implements CommandExecutor {

    private final PlayerPermissionChecker permissionChecker;
    private final LanguageConfiguration languageConfiguration;
    private final Reloadable reloadable;

    public SerenaReloadCommand(PlayerPermissionChecker permissionChecker, LanguageConfiguration languageConfiguration, Reloadable reloadable) {
        this.permissionChecker = permissionChecker;
        this.languageConfiguration = languageConfiguration;
        this.reloadable = reloadable;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the
     * player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     *
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.languageConfiguration.prefix() + "Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        if (!permissionChecker.useSerenaReloadCommand(player)) {
            player.sendMessage(this.languageConfiguration.permissionMissing());
            return true;
        }

        this.reloadable.reload();
        player.sendMessage(this.languageConfiguration.reloadComplete());
        return true;
    }
}
