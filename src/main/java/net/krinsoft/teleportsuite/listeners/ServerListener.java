package net.krinsoft.teleportsuite.listeners;

import com.fernferret.allpay.AllPay;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.Arrays;

/**
 * @author krinsdeath
 */
public class ServerListener extends org.bukkit.event.server.ServerListener {
    private TeleportSuite plugin;

    public ServerListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (Arrays.asList(AllPay.validEconPlugins).contains(event.getPlugin().getDescription().getName())) {
            plugin.log("Detected " + event.getPlugin().getDescription().getName() + " v" + event.getPlugin().getDescription().getVersion() + "; attempting to hook...");
            plugin.validateAllPay();
        }
    }
    
    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        if (Arrays.asList(AllPay.validEconPlugins).contains(event.getPlugin().getDescription().getName())) {
            plugin.log("Detected " + event.getPlugin().getDescription().getName() + " disabling... unhooking.");
            plugin.validateAllPay(false);
        }
    }
}
