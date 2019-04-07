package me.ravvenlord.serena.configuration.language;

import me.ravvenlord.serena.configuration.ConfigurationFactory;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.regex.Pattern;

public class ImmutableLanguageConfigurationFactory implements ConfigurationFactory<LanguageConfiguration> {

    private static final Pattern INTEGER_FORMAT = Pattern.compile("^.*(%d).*$");
    private static final Pattern STRING_FORMAT = Pattern.compile("^.*(%s).*$");
    private static final Pattern POST_WHITESPACE = Pattern.compile("^.* $");

    /**
     * Creates the instance of the configuration data object
     *
     * @param configuration the spigot configuration instance to base it on
     *
     * @return the created instance
     */
    @Override
    public LanguageConfiguration create(Configuration configuration) {
        return new ImmutableLanguageConfiguration(
                ensurePostWhitespace(colour(getString(configuration, "prefix")))
                , ensureOctal(colour(getString(configuration, "player-pickup-cooldown")))
                , colour(getString(configuration, "pickup-disabled"))
                , colour(getString(configuration, "pickup-enabled"))
                , colour(getString(configuration, "permission-missing"))
                , ensureString(colour(getString(configuration, "player-cannot-be-picked-up")))
                , colour(getString(configuration, "reload-complete")));
    }

    /**
     * Colours the provided string in the minecraft chat colors
     *
     * @param string the string to colour
     *
     * @return the converted string
     */
    private String colour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string) + ChatColor.RESET;
    }

    /**
     * Ensures that the string contains an octal format placeholder
     *
     * @param string the string to check
     *
     * @return the ensured string
     */
    private String ensureOctal(String string) {
        return ensureRegex(INTEGER_FORMAT, string, ": %o");
    }

    /**
     * Ensure whitespace ensures that there is a whitespace at the end of the string
     *
     * @param string the string to ensure
     *
     * @return the ensured string
     */
    private String ensurePostWhitespace(String string) {
        return ensureRegex(POST_WHITESPACE, string, " ");
    }

    /**
     * Ensures that the string contains a string formatter
     *
     * @param string the string to ensure
     *
     * @return the ensured string
     */
    private String ensureString(String string) {
        return ensureRegex(STRING_FORMAT, string, ": %s");
    }

    /**
     * Ensures a string to match the pattern and returns it either unmodified or adds the fix string at the end if the
     * pattern failed
     *
     * @param pattern the pattern to check with
     * @param string the string to check against
     * @param fix the fix string to appand
     *
     * @return the ensured string
     */
    private String ensureRegex(Pattern pattern, String string, String fix) {
        return pattern.matcher(string).matches() ? string : string + fix;
    }

    /**
     * Returns the string found in the config or a very basic default message based on the path
     *
     * @param configuration the configuration to read from
     * @param path the path to lookup
     *
     * @return the string
     */
    private String getString(Configuration configuration, String path) {
        return configuration.getString(path, "{" + path + "}");
    }
}
