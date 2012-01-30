package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
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
        setCommandUsage("/tptoggle -[acc|rej|auto]");
        addCommandExample("/tptoggle -acc -- Set yourself to allow requests.");
        addCommandExample("/tptoggle -rej -- Set yourself to ignore all requests.");
        addCommandExample("/tptoggle -- Flip between accepting and rejecting requests.");
        setArgRange(0, 1);
        addKey("teleport toggle");
        addKey("tps toggle");
        addKey("tptoggle");
        addKey("toggle");
        addKey("tptog");
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
                case REJECTING: p.setStatus(TeleportPlayer.Status.ACCEPTING); break;
                case AUTO: p.setStatus(TeleportPlayer.Status.ACCEPTING); break;
            }
        } else {
            if (args.get(0).startsWith("-acc")) {
                status = TeleportPlayer.Status.ACCEPTING;
            } else if (args.get(0).startsWith("-aut")) {
                status = TeleportPlayer.Status.AUTO;
            } else if (args.get(0).startsWith("-rej")) {
                status = TeleportPlayer.Status.REJECTING;
            } else {
                status = TeleportPlayer.Status.ACCEPTING;
            }
            p.setStatus(status);
        }
    }
}
