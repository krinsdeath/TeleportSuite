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
public class TPBackCommand extends TeleportCommand {
    
    public TPBackCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Back");
        setCommandUsage("/tpback");
        setArgRange(0, 0);
        addKey("teleport back");
        addKey("tps back");
        addKey("tpback");
        addKey("back");
        addKey("tpb");
        setPermission("teleport.back", "Allows users to teleport to their last known location.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer p = manager.getPlayer(sender.getName());
        p.rewind();
    }
}
