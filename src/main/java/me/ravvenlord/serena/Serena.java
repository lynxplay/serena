package me.ravvenlord.serena;

import me.ravvenlord.serena.commands.SerenaReloadCommand;
import me.ravvenlord.serena.commands.SerenaToggleCommand;
import me.ravvenlord.serena.configuration.ConfigurationFactory;
import me.ravvenlord.serena.configuration.language.ImmutableLanguageConfigurationFactory;
import me.ravvenlord.serena.configuration.language.LanguageConfiguration;
import me.ravvenlord.serena.configuration.properties.ImmutablePropertyConfigurationFactory;
import me.ravvenlord.serena.configuration.properties.PropertyConfiguration;
import me.ravvenlord.serena.cooldown.CooldownContainer;
import me.ravvenlord.serena.cooldown.HashedCooldownContainer;
import me.ravvenlord.serena.listener.PlayerConnectionListener;
import me.ravvenlord.serena.listener.PlayerPickupListener;
import me.ravvenlord.serena.listener.PlayerThrowListener;
import me.ravvenlord.serena.permission.PlayerPermissionChecker;
import me.ravvenlord.serena.permission.SimplePlayerPermissionChecker;
import me.ravvenlord.serena.player.PlayerToggleRegistry;
import me.ravvenlord.serena.player.QueuePlayerToggleRegistry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Serena extends JavaPlugin implements Reloadable {

    private Path languageConfigPath;
    private Path propertiesConfigPath;

    private ConfigurationFactory<LanguageConfiguration> languageConfigurationFactory;
    private ConfigurationFactory<PropertyConfiguration> propertyConfigurationFactory;

    private CooldownContainer cooldownContainer;
    private PlayerToggleRegistry playerToggleRegistry;
    private PlayerPermissionChecker playerPermissionChecker;

    private FixedScheduler fixedScheduler;

    @Override
    public void onEnable() {
        this.languageConfigPath = getDataFolder().toPath().resolve("language.yml");
        this.propertiesConfigPath = getDataFolder().toPath().resolve("properties.yml");

        this.languageConfigurationFactory = new ImmutableLanguageConfigurationFactory();
        this.propertyConfigurationFactory = new ImmutablePropertyConfigurationFactory();

        this.cooldownContainer = new HashedCooldownContainer();
        this.playerPermissionChecker = new SimplePlayerPermissionChecker();
        this.playerToggleRegistry = new QueuePlayerToggleRegistry();

        this.fixedScheduler = (r, l) -> getServer().getScheduler().runTaskLater(this, r, l);

        this.reload();
    }

    @Override
    public void reload() {
        exportConfiguration();

        LanguageConfiguration languageConfiguration = readConfig(languageConfigurationFactory, "language.yml");
        PropertyConfiguration propertyConfiguration = readConfig(propertyConfigurationFactory, "properties.yml");

        getLogger().info(String.format("player-pickup-cooldown %d seconds", propertyConfiguration.playerPickupCooldown().getSeconds()));

        HandlerList.unregisterAll(this);
        registerListeners(this.cooldownContainer, languageConfiguration, this.playerToggleRegistry
                , this.playerPermissionChecker, propertyConfiguration, fixedScheduler);

        registerCommands(this.playerToggleRegistry, languageConfiguration, this.playerPermissionChecker);
    }

    /**
     * Registers the listeners this plugin uses
     *
     * @param cooldownContainer the cooldown container to pass
     * @param languageConfiguration the languageConfiguration to use
     * @param toggleRegistry the toggle registry to use
     * @param permissionChecker the permission checker to use
     * @param propertyConfiguration the propertyConfiguration to use
     */
    private void registerListeners(CooldownContainer cooldownContainer
            , LanguageConfiguration languageConfiguration
            , PlayerToggleRegistry toggleRegistry
            , PlayerPermissionChecker permissionChecker
            , PropertyConfiguration propertyConfiguration
            , FixedScheduler scheduler) {

        this.getServer().getPluginManager().registerEvents(new PlayerPickupListener(cooldownContainer
                , languageConfiguration
                , toggleRegistry
                , permissionChecker
                , propertyConfiguration
                , Player::getDisplayName), this);
        this.getServer().getPluginManager().registerEvents(new PlayerThrowListener(propertyConfiguration, scheduler), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(cooldownContainer), this);
    }

    /**
     * Registers the commands this plugin uses
     *
     * @param playerToggleRegistry the toggle registry to use
     * @param languageConfiguration the languageConfiguration to use
     * @param permissionChecker the the permission checker to use
     */
    private void registerCommands(PlayerToggleRegistry playerToggleRegistry
            , LanguageConfiguration languageConfiguration
            , PlayerPermissionChecker permissionChecker) {

        Objects.requireNonNull(getCommand("serena")).setExecutor(new SerenaToggleCommand(playerToggleRegistry
                , languageConfiguration
                , permissionChecker));
        Objects.requireNonNull(getCommand("serena-reload")).setExecutor(new SerenaReloadCommand(permissionChecker
                , languageConfiguration
                , this));
    }

    /**
     * Exports all the default configurations
     */
    private void exportConfiguration() {
        if (!Files.exists(languageConfigPath)) this.saveResource("language.yml", false);
        if (!Files.exists(propertiesConfigPath)) this.saveResource("properties.yml", false);
    }

    /**
     * Reads a config from a relative path
     *
     * @param factory the factory to create the data instance from
     * @param filePath the file path of the config
     * @param <T> the type of the data instance
     *
     * @return the data instance
     *
     * @throws RuntimeException if the file could not be read
     */
    private <T> T readConfig(ConfigurationFactory<T> factory, String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(getDataFolder().toPath().resolve(filePath))) {
            return factory.create(YamlConfiguration.loadConfiguration(reader));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not read configuration file %s in plugin data-folder", filePath), e);
        }
    }
}
