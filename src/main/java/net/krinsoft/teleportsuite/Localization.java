package net.krinsoft.teleportsuite;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author krinsdeath
 */
public class Localization {
    private final Pattern colors = Pattern.compile("&([0-9A-F])");
    private final Pattern target = Pattern.compile("\\[target\\]");

    private TeleportSuite plugin;
    private String name;
    private FileConfiguration language;
    
    public Localization(TeleportSuite plugin, String name, FileConfiguration language) {
        this.plugin = plugin;
        this.name = name;
        this.language = language;
    }

    public Object get(String key) {
        return language.get(key, null);
    }

    public void update() {
        if (plugin.getDescription().getVersion().startsWith("2.2")) {
            language.set("teleport.timeout.to", "&AYour request to &A[target] has &Ctimed out&A.");
            language.set("teleport.timeout.from", "&A[target]'s teleport request has &Ctimed out&A.");
            plugin.debug("Updating '" + name + "' to Localization v2.2");
        }
    }

    public String get(String key, String token) {
        try {
            String replaced = language.getString(key);
            if (replaced != null) {
                replaced = target.matcher(replaced).replaceAll(token);
                replaced = colors.matcher(replaced).replaceAll("\u00A7$1");
            }
            return replaced;
        } catch (NullPointerException e) {
            plugin.debug("Encountered null path at '" + key + "'");
        }
        return null;
    }

    public void save() {
        try {
            language.save(new File(plugin.getDataFolder(), name));
        } catch (IOException e) {
            plugin.debug("Error saving language file '" + name + "'");
        }
    }

}
