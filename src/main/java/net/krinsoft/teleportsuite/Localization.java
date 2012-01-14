package net.krinsoft.teleportsuite;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Pattern;

/**
 * @author krinsdeath
 */
public class Localization {
    private final Pattern colors = Pattern.compile("&([0-9A-F])");
    private final Pattern target = Pattern.compile("\\[target\\]");
    
    private TeleportSuite plugin;
    private FileConfiguration language;
    
    public Localization(TeleportSuite plugin, FileConfiguration language) {
        this.plugin = plugin;
        this.language = language;
    }
    
    public String error(String key, String token) {
        try {
            String replaced = language.getString("errors." + key);
            if (replaced != null) {
                replaced = target.matcher(replaced).replaceAll(token);
                replaced = colors.matcher(replaced).replaceAll("\u00A7$1");
            }
            return replaced;
        } catch (NullPointerException e) {
            plugin.debug("Bug Bukkit about pull request #455");
        }
        return null;
    }
    
    public String teleport(String key, String token) {
        try {
            String replaced = language.getString("teleport." + key);
            if (replaced != null) {
                replaced = target.matcher(replaced).replaceAll(token);
                replaced = colors.matcher(replaced).replaceAll("\u00A7$1");
            }
            return replaced;
        } catch (NullPointerException e) {
            plugin.debug("Bug Bukkit about pull request #455");
        }
        return null;
    }

}
