package net.krinsoft.teleportsuite;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 *
 * @author krinsdeath
 */
public class TeleportSuite extends JavaPlugin {
    private final static Logger LOGGER = Logger.getLogger("TeleportSuite");
    private boolean debug = true;

    @Override
    public void onEnable() {
        long startup = System.currentTimeMillis();
        if (!validateCommandHandler()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        log("Enabled in " + (System.currentTimeMillis() - startup) + "ms");
    }

    @Override
    public void onDisable() {

    }
    
    public void log(String message) {
        message = "[" + this + "] " + message;
        LOGGER.info(message);
    }
    
    public void debug(String message) {
        if (debug) {
            message = "[" + this + "] [Debug] " + message;
            LOGGER.info(message);
        }
    }

    private boolean validateCommandHandler() {
        double CHVersion = 3.0;
        try {
            
        } catch (Throwable t) {
            return false;
        }
        return true;
    }
}
