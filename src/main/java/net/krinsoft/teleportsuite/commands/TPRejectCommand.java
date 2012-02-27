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
public class TPRejectCommand extends TeleportCommand {

    public TPRejectCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Reject");
        setCommandUsage(  ChatColor.GREEN + "/reject" + ChatColor.GOLD + " [OPTION]");
        addCommandExample(ChatColor.GREEN + "/reject" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Rejects a request from 'Njodi'");
        addCommandExample(ChatColor.GREEN + "/reject" + ChatColor.GOLD + " -all    " + ChatColor.WHITE + " -- Rejects all pending requests.");
        addCommandExample(ChatColor.GREEN + "/reject" + ChatColor.GOLD + "         " + ChatColor.WHITE + " -- Rejects your first open request.");
        setArgRange(0, 1);
        addKey("teleport reject");
        addKey("tps reject");
        addKey("tpreject");
        addKey("reject");
        addKey("tprej");
        addKey("rej");
        setPermission("teleport.reject", "Allows this user to access the /tpreject command", PermissionDefault.TRUE);
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
                    manager.finish(p, req, false);
                }
                return;
            } else {
                if (p.getRequest(args.get(0)) != null) {
                    r = p.getRequest(args.get(0));
                    if (r == null) {
                        p.sendLocalizedString("error.requests.failed", args.get(0));
                    }
                }
            }
        }
        manager.finish(p, r, false);
    }
}
