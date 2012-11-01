package net.krinsoft.teleportsuite.listeners;

import com.fernferret.allpay.AllPay;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class ServerListener implements Listener {
    private TeleportSuite plugin;

    public ServerListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void pluginEnable(PluginEnableEvent event) {
        if (Arrays.asList(AllPay.getValidEconPlugins()).contains(event.getPlugin().getDescription().getName())) {
            plugin.log("Detected " + event.getPlugin().getDescription().getName() + " v" + event.getPlugin().getDescription().getVersion() + "; attempting to hook...");
            plugin.validateAllPay();
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void pluginDisable(PluginDisableEvent event) {
        if (Arrays.asList(AllPay.getValidEconPlugins()).contains(event.getPlugin().getDescription().getName())) {
            plugin.log("Detected " + event.getPlugin().getDescription().getName() + " disabling... unhooking.");
            plugin.validateAllPay(false);
        }
    }
}
