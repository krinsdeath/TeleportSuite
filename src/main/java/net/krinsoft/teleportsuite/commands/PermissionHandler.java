package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.PermissionsInterface;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 *
 * @author krinsdeath
 */
public class PermissionHandler implements PermissionsInterface {
    private TeleportSuite plugin;
    
    public PermissionHandler(TeleportSuite plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String node, boolean isOpRequired) {
        return sender.hasPermission(node) || (sender.isOp() && !sender.isPermissionSet(node));
    }

    @Override
    public boolean hasAnyPermission(CommandSender sender, List<String> nodes, boolean opRequired) {
        for (String node : nodes) {
            if (hasPermission(sender, node, opRequired)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAllPermission(CommandSender sender, List<String> nodes, boolean opRequired) {
        for (String node : nodes) {
            if (!hasPermission(sender, node, opRequired)) {
                return false;
            }
        }
        return true;
    }
}
