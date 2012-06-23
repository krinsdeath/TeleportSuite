package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * @author krinsdeath
 */
public class TPAcceptCommand extends TeleportCommand {
    
    public TPAcceptCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Accept");
        setCommandUsage(  ChatColor.GREEN + "/accept" + ChatColor.GOLD + " [OPTION]");
        addCommandExample(ChatColor.GREEN + "/accept" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Accepts a request from 'Njodi'");
        addCommandExample(ChatColor.GREEN + "/accept" + ChatColor.GOLD + " -all    " + ChatColor.WHITE + " -- Accepts all pending requests.");
        addCommandExample(ChatColor.GREEN + "/accept" + ChatColor.GOLD + "         " + ChatColor.WHITE + " -- Accepts your first open request.");
        setArgRange(0, 1);
        addKey("teleport accept");
        addKey("tps accept");
        addKey("tpaccept");
        addKey("accept");
        addKey("tpacc");
        addKey("acc");
        setPermission("teleport.accept", "Allows the user to accept requests with /accept", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        TeleportPlayer p = manager.getPlayer(sender.getName());
        Request r = null;
        if (args.size() == 0) {
            if (!p.getRequests().isEmpty()) {
                r = p.getRequests().get(0);
            } else {
                p.sendLocalizedString("error.requests.none", p.getName());
                return;
            }
        } else {
            if (args.get(0).equals("-all") && !p.getRequests().isEmpty()) {
                for (Request req : new ArrayList<Request>(p.getRequests())) {
                    manager.finish(p, req, true);
                }
                return;
            } else {
                CommandSender target = plugin.getServer().getPlayer(args.get(0));
                if (target != null) {
                    r = p.getRequest(target.getName());
                    if (r == null) {
                        p.sendLocalizedString("error.requests.failed", args.get(0));
                        return;
                    }
                }
            }
        }
        manager.finish(p, r, true);
    }
}
