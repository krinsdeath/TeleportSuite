package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
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
        setCommandUsage("/tpaccept [PLAYER] [-all]");
        addCommandExample("/tpaccept Player -- Accepts a request from 'Player'");
        addCommandExample("/tpaccept -all -- Accepts all pending requests.");
        addCommandExample("/tpaccept -- Accepts your first open request.");
        setArgRange(0, 1);
        addKey("teleport accept");
        addKey("tps accept");
        addKey("tpaccept");
        addKey("accept");
        addKey("tpacc");
        setPermission("teleport.accept", "Allows this user to access the /tpaccept command", PermissionDefault.TRUE);
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
                if (p.getRequest(args.get(0)) != null) {
                    r = p.getRequest(args.get(0));
                    if (r == null) {
                        p.sendLocalizedString("error.requests.failed", args.get(0));
                    }
                }
            }
        }
        manager.finish(p, r, true);
    }
}
