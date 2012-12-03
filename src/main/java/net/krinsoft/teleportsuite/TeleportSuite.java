package net.krinsoft.teleportsuite;

import com.fernferret.allpay.AllPay;
import com.fernferret.allpay.GenericBank;
import com.pneumaticraft.commandhandler.CommandHandler;
import net.krinsoft.teleportsuite.commands.*;
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

/**
 *
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class TeleportSuite extends JavaPlugin {
    private boolean                     debug           = true;
    private boolean                     economy         = false;

    // main configuration
    private FileConfiguration           configuration   = null;
    private File                        configFile      = null;

    // user configuration
    private FileConfiguration           users           = null;
    private File                        userFile        = null;

    private Map<String, Localization>   languages       = new HashMap<String, Localization>();

    private TeleportManager             manager         = null;

    private GenericBank                 bank            = null;
    
    private PlayerListener              pListener       = new PlayerListener(this);
    private ServerListener              sListener       = new ServerListener(this);
    
    private CommandHandler              commands        = null;

    @Override
    public void onEnable() {
        long startup = System.currentTimeMillis();

        registerConfiguration();

        manager = new TeleportManager(this);
        
        registerEvents();
        registerCommands();
        log("Enabled in " + (System.currentTimeMillis() - startup) + "ms.");

        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                buildWorldPermissions();
            }
        }, 1L);
    }

    @Override
    public void onDisable() {
        long disable = System.currentTimeMillis();
        commands = null;
        sListener = null;
        pListener = null;
        bank = null;
        manager.disable();
        log("Disabled in " + (System.currentTimeMillis() - disable) + "ms.");
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        debug("[Command] " + cs.getName() + ": /" + label + " " + Arrays.toString(args));
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        arguments.add(0, label);
        return commands.locateAndRunCommand(cs, arguments);
    }

    @Override
    public FileConfiguration getConfig() {
        if (configuration == null) {
            configuration = YamlConfiguration.loadConfiguration(configFile);
        }
        return configuration;
    }

    @Override
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            warn("An error occurred while writing the config file to disk.");
            e.printStackTrace();
        }
    }
    
    public void log(String message) {
        getLogger().info(message);
    }
    
    public void warn(String message) {
        getLogger().warning(message);
    }
    
    public void debug(String message) {
        if (debug) {
            message = "[Debug] " + message;
            getLogger().info(message);
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

    public void registerConfiguration(boolean val) {
        if (val) {
            configuration = null;
            configFile = null;
            registerConfiguration();
        }
    }

    private void registerConfiguration() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getConfig().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/config.yml")));
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        if (getConfig().getString("plugin.version") != null) {
            getConfig().set("plugin", null);
            getConfig().set("teleport", null);
            getConfig().set("request", null);
            getConfig().set("error", null);
            getConfig().set("message", null);
            getConfig().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/config.yml")));
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        if (getConfig().get("timeouts.tpa") == null) {
            getConfig().set("plugin.timeouts", false);
            getConfig().set("timeouts.tpa", 30);
            getConfig().set("timeouts.tpahere", 30);
            saveConfig();
        }

        userFile = new File(getDataFolder(), "users.yml");
        if (!userFile.exists()) {
            getUsers().setDefaults(YamlConfiguration.loadConfiguration(this.getClass().getResourceAsStream("/users.yml")));
            getUsers().options().copyDefaults(true);
            saveUsers();
        }

        buildLocalizations();
        
        debug = getConfig().getBoolean("plugin.debug");
        economy = getConfig().getBoolean("plugin.economy");

        buildWorldPermissions();
    }

    public void buildWorldPermissions() {
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
                if (lang.getConfigurationSection("default") == null) {
                    lang.set("default.name", "American English");
                    lang.set("default.languages", "Available Languages:");
                    lang.set("default.change", "Localization set to American English");
                    lang.set("error.invalid.language", "&CThere is no such language available.");
                    log("New values in " + language + ".yml... customize them!");
                }
                languages.put(language, new Localization(this, language+".yml", lang));
            }
        }
        for (Localization l : languages.values()) {
            // 2.2 update - b97+
            if (l.get("teleport.timeout") == null) {
                l.update();
            }
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
        
        // player events
        pm.registerEvents(pListener, this);

        // server events
        pm.registerEvents(sListener, this);
    }
    
    private void registerCommands() {
        PermissionHandler permissions = new PermissionHandler();
        commands = new CommandHandler(this, permissions);
        // command help class
        commands.registerCommand(new BaseCommand(this));
        commands.registerCommand(new LocalizationCommand(this));
        // normal commands
        commands.registerCommand(new TPAcceptCommand(this));
        commands.registerCommand(new TPACommand(this));
        commands.registerCommand(new TPAHereCommand(this));
        commands.registerCommand(new TPBackCommand(this));
        commands.registerCommand(new TPCancelCommand(this));
        commands.registerCommand(new TPCommand(this));
        commands.registerCommand(new TPCoordsCommand(this));
        commands.registerCommand(new TPDebugCommand(this));
        commands.registerCommand(new TPHereCommand(this));
        commands.registerCommand(new TPLocationCommand(this));
        commands.registerCommand(new TPOCommand(this));
        commands.registerCommand(new TPOHereCommand(this));
        commands.registerCommand(new TPRejectCommand(this));
        commands.registerCommand(new TPReloadCommand(this));
        commands.registerCommand(new TPRequestsCommand(this));
        commands.registerCommand(new TPRewindCommand(this));
        commands.registerCommand(new TPSilentCommand(this));
        commands.registerCommand(new TPToggleCommand(this));
        commands.registerCommand(new TPVanillaCommand(this));
        commands.registerCommand(new TPWorldCommand(this));
        //commands.registerCommand(new TPGroupCommand(this));
        commands.registerCommand(new TPMCommand(this));
        commands.registerCommand(new TPMHereCommand(this));
    }
    
    public FileConfiguration getUsers() {
        if (users == null) {
            users = YamlConfiguration.loadConfiguration(userFile);
        }
        return users;
    }

    public void saveUsers() {
        try {
            getUsers().save(userFile);
        } catch (IOException e) {
            debug("An error occurred while saving 'users.yml'");
            e.printStackTrace();
        }
    }

    public boolean validateAllPay() {
        if (economy) {
            if (bank != null) { return true; }
            AllPay allpay = new AllPay(this, "[" + this + "] ");
            bank = allpay.loadEconPlugin();
            log(bank.getEconUsed() + " hooked successfully!");
            return true;
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
