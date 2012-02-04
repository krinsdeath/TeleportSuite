package net.krinsoft.teleportsuite;

import com.fernferret.allpay.AllPay;
import com.fernferret.allpay.GenericBank;
import com.pneumaticraft.commandhandler.CommandHandler;
import net.krinsoft.teleportsuite.commands.*;
import net.krinsoft.teleportsuite.listeners.EntityListener;
import net.krinsoft.teleportsuite.listeners.PlayerListener;
import net.krinsoft.teleportsuite.listeners.ServerListener;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author krinsdeath
 */
public class TeleportSuite extends JavaPlugin {
    private final static Logger LOGGER = Logger.getLogger("TeleportSuite");
    private boolean debug = true;
    private boolean economy = false;

    private Map<String, Localization> languages = new HashMap<String, Localization>();
    private FileConfiguration users;
    
    private TeleportManager manager;

    private GenericBank bank = null;
    
    private EntityListener eListener = new EntityListener(this);
    private PlayerListener pListener = new PlayerListener(this);
    private ServerListener sListener = new ServerListener(this);
    
    private CommandHandler commands;

    @Override
    public void onEnable() {
        long startup = System.currentTimeMillis();
        
        manager = new TeleportManager(this);
        
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
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        debug("[Command] " + cs.getName() + ": /" + label + " " + Arrays.toString(args));
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        arguments.add(0, label);
        return commands.locateAndRunCommand(cs, arguments);
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
    
    public void toggleDebug(CommandSender sender) {
        debug = !debug;
        getConfig().set("plugin.debug", debug);
        saveConfig();
        if (debug) {
            log("Debug mode enabled.");
            sender.sendMessage("Debug mode enabled.");
        } else {
            log("Debug mode disabled.");
            sender.sendMessage("Debug mode disabled.");
        }
    }
    
    private void registerConfiguration() {
        getConfig().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/config.yml")));
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
        }
        if (getConfig().getString("plugin.version") != null) {
            getConfig().set("plugin", null);
            getConfig().set("teleport", null);
            getConfig().set("request", null);
            getConfig().set("error", null);
            getConfig().set("message", null);
            getConfig().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/config.yml")));
            getConfig().options().copyDefaults(true);
        }
        saveConfig();
        
        getUsers().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/users.yml")));
        if (!new File(getDataFolder(), "users.yml").exists()) {
            getUsers().options().copyDefaults(true);
        }
        saveUsers();
        
        buildLocalizations();
        
        debug = getConfig().getBoolean("plugin.debug");
        economy = getConfig().getBoolean("plugin.economy");
        
        Permission worlds = new Permission("teleport.world.*");
        worlds.setDescription("Any user with this node can access all worlds");
        worlds.setDefault(PermissionDefault.TRUE);
        if (getServer().getPluginManager().getPermission(worlds.getName()) == null) {
            getServer().getPluginManager().addPermission(worlds);
        }
        for (World w : getServer().getWorlds()) {
            Permission world = new Permission("teleport.world." + w.getName());
            world.setDescription("Any user with this node can access the world '" + w.getName() + "'");
            world.setDefault(PermissionDefault.TRUE);
            worlds.getChildren().put(world.getName(), true);
            if (getServer().getPluginManager().getPermission(world.getName()) == null) {
                getServer().getPluginManager().addPermission(world);
            }
        }
        worlds.recalculatePermissibles();
    }
    
    private void buildLocalizations() {
        for (String language : getConfig().getStringList("languages.available")) {
            File f = new File(getDataFolder(), language+".yml");
            InputStream in = this.getClass().getResourceAsStream("/"+language+".yml");
            if (in != null) {
                FileConfiguration lang = YamlConfiguration.loadConfiguration(f);
                lang.setDefaults(YamlConfiguration.loadConfiguration(in));
                if (!f.exists()) {
                    lang.options().copyDefaults(true);
                }
                languages.put(language, new Localization(this, language+".yml", lang));
            }
        }
        for (Localization l : languages.values()) {
            l.save();
        }
    }
    
    public Localization getLocalization(String language) {
        if (language == null) { language = getConfig().getString("languages.default"); }
        if (languages.containsKey(language)) {
            return languages.get(language);
        } else {
            return languages.get(getConfig().getString("languages.default"));
        }
    }
    
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        
        // entity events
        pm.registerEvents(eListener, this);
        
        // player events
        pm.registerEvents(pListener, this);

        // server events
        pm.registerEvents(sListener, this);
    }
    
    private void registerCommands() {
        PermissionHandler permissions = new PermissionHandler(this);
        commands = new CommandHandler(this, permissions);
        commands.registerCommand(new TPACommand(this));
        commands.registerCommand(new TPAHereCommand(this));
        commands.registerCommand(new TPCommand(this));
        commands.registerCommand(new TPHereCommand(this));
        commands.registerCommand(new TPOCommand(this));
        commands.registerCommand(new TPOHereCommand(this));
        commands.registerCommand(new TPWorldCommand(this));
        commands.registerCommand(new TPAcceptCommand(this));
        commands.registerCommand(new TPRejectCommand(this));
        commands.registerCommand(new TPCancelCommand(this));
        commands.registerCommand(new TPSilentCommand(this));
        commands.registerCommand(new TPToggleCommand(this));
        commands.registerCommand(new TPRequestsCommand(this));
        commands.registerCommand(new TPBackCommand(this));
        commands.registerCommand(new TPRewindCommand(this));
        commands.registerCommand(new TPVanillaCommand(this));
        commands.registerCommand(new TPDebugCommand(this));
        commands.registerCommand(new TPLocationCommand(this));
        //commands.registerCommand(new TPGroupCommand(this));
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
            if (bank != null) { return true; }
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
    
    public FileConfiguration getUsers() {
        if (users == null) {
            users = YamlConfiguration.loadConfiguration(new File(getDataFolder(),  "users.yml"));
        }
        return users;
    }

    public void saveUsers() {
        try {
            users.save(new File(getDataFolder(), "users.yml"));
        } catch (IOException e) {
            debug("An error occurred while saving 'users.yml'");
            e.printStackTrace();
        }
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
        validateAllPay();
        return bank;
    }
    
    public TeleportManager getManager() {
        return manager;
    }
    
    public CommandHandler getHandler() {
        return commands;
    }
}
