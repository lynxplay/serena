package dev.lynxplay.serena;

import dev.lynxplay.serena.commands.SerenaReloadCommand;
import dev.lynxplay.serena.commands.SerenaToggleCommand;
import dev.lynxplay.serena.configuration.ConfigurationFactory;
import dev.lynxplay.serena.configuration.language.ImmutableLanguageConfigurationFactory;
import dev.lynxplay.serena.configuration.language.LanguageConfiguration;
import dev.lynxplay.serena.configuration.properties.ImmutablePropertyConfigurationFactory;
import dev.lynxplay.serena.configuration.properties.PropertyConfiguration;
import dev.lynxplay.serena.cooldown.CooldownContainer;
import dev.lynxplay.serena.cooldown.HashedCooldownContainer;
import dev.lynxplay.serena.listener.PlayerConnectionListener;
import dev.lynxplay.serena.listener.PlayerPickupListener;
import dev.lynxplay.serena.listener.PlayerThrowListener;
import dev.lynxplay.serena.permission.PlayerPermissionChecker;
import dev.lynxplay.serena.permission.SimplePlayerPermissionChecker;
import dev.lynxplay.serena.player.PersistentPlayerToggleRegister;
import dev.lynxplay.serena.player.PlayerToggleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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
        this.playerToggleRegistry = new PersistentPlayerToggleRegister(Bukkit::getPlayer, new NamespacedKey(this,
            "ToggleValue"));

        this.fixedScheduler = (r, l) -> getServer().getScheduler().runTaskLater(this, r, l);

        this.reload();
    }

    @Override
    public void reload() {
        exportConfiguration();

        final LanguageConfiguration languageConfiguration = readConfig(languageConfigurationFactory, "language.yml");
        final PropertyConfiguration propertyConfiguration = readConfig(propertyConfigurationFactory, "properties.yml");

        getLogger().info(String.format("player-pickup-cooldown %d seconds",
            propertyConfiguration.playerPickupCooldown().getSeconds()));

        HandlerList.unregisterAll(this);
        registerListeners(
            this.cooldownContainer,
            languageConfiguration,
            this.playerToggleRegistry,
            this.playerPermissionChecker,
            propertyConfiguration,
            fixedScheduler
        );

        registerCommands(this.playerToggleRegistry, languageConfiguration, this.playerPermissionChecker);
    }

    /**
     * Registers the listeners this plugin uses
     *
     * @param cooldownContainer     the cooldown container to pass
     * @param languageConfiguration the languageConfiguration to use
     * @param toggleRegistry        the toggle registry to use
     * @param permissionChecker     the permission checker to use
     * @param propertyConfiguration the propertyConfiguration to use
     */
    private void registerListeners(final CooldownContainer cooldownContainer,
                                   final LanguageConfiguration languageConfiguration,
                                   final PlayerToggleRegistry toggleRegistry,
                                   final PlayerPermissionChecker permissionChecker,
                                   final PropertyConfiguration propertyConfiguration,
                                   final FixedScheduler scheduler) {

        this.getServer().getPluginManager().registerEvents(new PlayerPickupListener(
            cooldownContainer,
            languageConfiguration,
            toggleRegistry,
            permissionChecker,
            propertyConfiguration,
            Player::displayName
        ), this);
        this.getServer().getPluginManager().registerEvents(new PlayerThrowListener(propertyConfiguration, scheduler), this);
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(cooldownContainer), this);
    }

    /**
     * Registers the commands this plugin uses
     *
     * @param playerToggleRegistry  the toggle registry to use
     * @param languageConfiguration the languageConfiguration to use
     * @param permissionChecker     the the permission checker to use
     */
    private void registerCommands(final PlayerToggleRegistry playerToggleRegistry,
                                  final LanguageConfiguration languageConfiguration,
                                  final PlayerPermissionChecker permissionChecker) {

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
     * @param factory  the factory to create the data instance from
     * @param filePath the file path of the config
     * @param <T>      the type of the data instance
     *
     * @return the data instance
     *
     * @throws RuntimeException if the file could not be read
     */
    private <T> T readConfig(final ConfigurationFactory<T> factory, final String filePath) {
        try (final BufferedReader reader = Files.newBufferedReader(getDataFolder().toPath().resolve(filePath))) {
            return factory.create(YamlConfiguration.loadConfiguration(reader));
        } catch (final IOException e) {
            throw new RuntimeException(String.format("Could not read configuration file %s in plugin data-folder",
                filePath), e);
        }
    }

}
