package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
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
public class TPRequestsCommand extends TeleportCommand {
    
    public TPRequestsCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Requests");
        setCommandUsage(  ChatColor.GREEN + "/requests" + ChatColor.GOLD + " [PLAYER]");
        addCommandExample(ChatColor.GREEN + "/requests" + ChatColor.GOLD + "         " + ChatColor.WHITE + " -- Show all pending requests.");
        addCommandExample(ChatColor.GREEN + "/requests" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Show Njodi's requests.");
        setArgRange(0, 1);
        addKey("teleport requests");
        addKey("tps requests");
        addKey("tprequests");
        addKey("requests");
        addKey("tpreq");
        addKey("req");
        setPermission("teleport.requests", "Allows this user to view their open requests.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        String t;
        if (sender instanceof ConsoleCommandSender && args.size() < 1) {
            sender.sendMessage(plugin.getLocalization(null).get("error.invalid.arguments", sender.getName()));
            return;
        } else {
            if (args.size() == 0) {
                t = sender.getName();
            } else {
                t = args.get(0);
            }
        }
        TeleportPlayer target = manager.getPlayer(t);
        for (Request r : target.getRequests()) {
            target.sendMessage(r.getTo().getName() + ": /" + r.getType().getName());
        }
        if (target.isRequesting()) {
            target.sendLocalizedString("status.requests.active", target.getActive());
        }
    }
}
