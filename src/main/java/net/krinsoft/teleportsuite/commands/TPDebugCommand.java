package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPDebugCommand extends TeleportCommand {
    
    public TPDebugCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Debug");
        setCommandUsage("/tpdebug");
        setArgRange(0, 0);
        addKey("teleport debug");
        addKey("tps debug");
        addKey("tpdebug");
        setPermission("teleport.debug", "Allows the user to toggle 'debug' mode.", PermissionDefault.OP);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        plugin.toggleDebug(sender);
    }
}
