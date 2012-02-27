package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPReloadCommand extends TeleportCommand {

    public TPReloadCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Reload");
        setCommandUsage(  ChatColor.GREEN + "/tpreload");
        addCommandExample(ChatColor.GREEN + "/tpreload" + ChatColor.WHITE + " -- Reload TeleportSuite's main configuration.");
        setArgRange(0, 0);
        addKey("teleport reload");
        addKey("tps reload");
        addKey("tpreload");
        setPermission("teleport.reload", "Allows you to reload TeleportSuite's configuration.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        long t = System.currentTimeMillis();
        plugin.registerConfiguration(true);
        t = System.currentTimeMillis() - t;
        sender.sendMessage(ChatColor.GREEN + "Reloaded TeleportSuite's configuration. (" + t + "ms)");
        plugin.debug(">> " + sender.getName() + ": Reloaded TeleportSuite's configuration. (" + t + "ms)");
    }
}
