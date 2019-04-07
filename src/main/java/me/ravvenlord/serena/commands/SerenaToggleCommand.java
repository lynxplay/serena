package me.ravvenlord.serena.commands;

import me.ravvenlord.serena.configuration.language.LanguageConfiguration;
import me.ravvenlord.serena.permission.PlayerPermissionChecker;
import me.ravvenlord.serena.player.PlayerToggleRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SerenaToggleCommand implements CommandExecutor {

    private final PlayerToggleRegistry playerToggleRegistry;
    private final LanguageConfiguration languageConfiguration;
    private final PlayerPermissionChecker permissionChecker;

    public SerenaToggleCommand(PlayerToggleRegistry playerToggleRegistry
            , LanguageConfiguration languageConfiguration
            , PlayerPermissionChecker permissionChecker) {
        this.playerToggleRegistry = playerToggleRegistry;
        this.languageConfiguration = languageConfiguration;
        this.permissionChecker = permissionChecker;
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
        if (!permissionChecker.useSerenaToggleCommand(player)) {
            player.sendMessage(this.languageConfiguration.permissionMissing());
            return true;
        }

        player.sendMessage(this.playerToggleRegistry.toggle(player.getUniqueId())
                ? this.languageConfiguration.pickupEnabled()
                : this.languageConfiguration.pickupDisabled());
        return true;
    }
}
