package net.krinsoft.teleportsuite;

import com.fernferret.allpay.AllPay;
import com.fernferret.allpay.GenericBank;
import com.pneumaticraft.commandhandler.CommandHandler;
import net.krinsoft.teleportsuite.commands.PermissionHandler;
import net.krinsoft.teleportsuite.commands.TPACommand;
import net.krinsoft.teleportsuite.listeners.EntityListener;
import net.krinsoft.teleportsuite.listeners.PlayerListener;
import net.krinsoft.teleportsuite.listeners.ServerListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 *
 * @author krinsdeath
 */
public class TeleportSuite extends JavaPlugin {
    private final static Logger LOGGER = Logger.getLogger("TeleportSuite");
    private boolean debug = true;
    private boolean economy = false;
    
    private GenericBank bank = null;
    
    private EntityListener eListener = new EntityListener(this);
    private PlayerListener pListener = new PlayerListener(this);
    private ServerListener sListener = new ServerListener(this);
    
    private CommandHandler commands;

    @Override
    public void onEnable() {
        long startup = System.currentTimeMillis();
        if (!validateCommandHandler()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerConfiguration();
        registerEvents();
        registerCommands();
        log("Enabled in " + (System.currentTimeMillis() - startup) + "ms");
    }

    @Override
    public void onDisable() {
        long disable = System.currentTimeMillis();
        commands = null;
        sListener = null;
        eListener = null;
        pListener = null;
        bank = null;
        log("Disabled TeleportSuite in " + (System.currentTimeMillis() - disable) + "ms");
    }
    
    public void log(String message) {
        message = "[" + this + "] " + message;
        LOGGER.info(message);
    }
    
    public void warn(String message) {
        message = "[" + this + "] " + message;
        LOGGER.warning(message);
    }
    
    public void debug(String message) {
        if (debug) {
            message = "[" + this + "] [Debug] " + message;
            LOGGER.info(message);
        }
    }
    
    private void registerConfiguration() {
        if (getConfig().get("plugin.version") == null || getConfig().getBoolean("plugin.rebuild")) {
            getConfig().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/config.yml")));
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        debug = getConfig().getBoolean("plugin.debug");
        economy = getConfig().getBoolean("plugin.economy");
    }
    
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        
        // entity events
        pm.registerEvent(Event.Type.ENTITY_DEATH, eListener, Event.Priority.Monitor, this);
        
        // player events
        pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, pListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, pListener, Event.Priority.Monitor, this);
        
        // server events
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, sListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, sListener, Event.Priority.Monitor, this);
    }
    
    private void registerCommands() {
        PermissionHandler permissions = new PermissionHandler(this);
        commands = new CommandHandler(this, permissions);
        commands.registerCommand(new TPACommand(this));
        //
    }

    private boolean validateCommandHandler() {
        double CHVersion = 3.0;
        CommandHandler ch = new CommandHandler(this, null);
        try {
            if (ch.getVersion() >= CHVersion) {
                return true;
            } else {
                warn("Another plugin initialized before TeleportSuite with an outdated version!");
                warn("TeleportSuite needs at least v" + CHVersion + ", but " + ch.getVersion() + " was found!");
                return false;
            }
        } catch (Throwable t) {
            warn("Another plugin initialized before TeleportSuite with an outdated version!");
            warn("TeleportSuite needs at least v" + CHVersion + ", but " + ch.getVersion() + " was found!");
            return false;
        }
    }
    
    public boolean validateAllPay() {
        if (economy) {
            double APVersion = 3.1;
            AllPay allpay = new AllPay(this, "[" + this + "] ");
            if (allpay.getVersion() >= APVersion) {
                bank = allpay.loadEconPlugin();
                log(bank.getEconUsed() + " hooked successfully!");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public boolean validateAllPay(boolean val) {
        if (!val) {
            bank = null;
            economy = false;
            return false;
        }
        return validateAllPay();
    }
    
    public GenericBank getBank() {
        if (!economy) { return null; }
        return bank;
    }
}
