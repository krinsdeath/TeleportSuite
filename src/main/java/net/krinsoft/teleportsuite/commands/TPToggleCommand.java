package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPToggleCommand extends TeleportCommand {
    
    public TPToggleCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Toggle");
        setCommandUsage(  ChatColor.GREEN + "/toggle" + ChatColor.GOLD + " [OPTION]");
        addCommandExample(ChatColor.GREEN + "/toggle" + ChatColor.GOLD + " acc" + ChatColor.WHITE + " -- Set yourself to allow requests.");
        addCommandExample(ChatColor.GREEN + "/toggle" + ChatColor.GOLD + " rej" + ChatColor.WHITE + " -- Automatically reject all requests.");
        addCommandExample(ChatColor.GREEN + "/toggle" + ChatColor.GOLD + " aut" + ChatColor.WHITE + " -- Automatically accept all requests.");
        addCommandExample(ChatColor.GREEN + "/toggle" + ChatColor.GOLD + "    " + ChatColor.WHITE + " -- Cycle between toggle statuses.");
        setArgRange(0, 1);
        addKey("teleport toggle");
        addKey("tps toggle");
        addKey("tptoggle");
        addKey("toggle");
        addKey("tptog");
        addKey("tog");
        setPermission("teleport.toggle", "Allows the user to toggle their request status.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer p = manager.getPlayer(sender.getName());
        TeleportPlayer.Status status;
        if (args.size() == 0) {
            status = p.getStatus();
            switch (status) {
                case ACCEPTING: p.setStatus(TeleportPlayer.Status.REJECTING); break;
                case REJECTING: p.setStatus(TeleportPlayer.Status.AUTO); break;
                case AUTO: p.setStatus(TeleportPlayer.Status.ACCEPTING); break;
            }
        } else {
            if (args.get(0).startsWith("acc")) {
                status = TeleportPlayer.Status.ACCEPTING;
            } else if (args.get(0).startsWith("aut")) {
                status = TeleportPlayer.Status.AUTO;
            } else if (args.get(0).startsWith("rej")) {
                status = TeleportPlayer.Status.REJECTING;
            } else {
                status = TeleportPlayer.Status.ACCEPTING;
            }
            p.setStatus(status);
        }
    }
}
