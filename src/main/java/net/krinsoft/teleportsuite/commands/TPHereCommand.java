package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPHereCommand extends TeleportCommand {

    public TPHereCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TP Here");
        setCommandUsage(  ChatColor.GREEN + "/tphere" + ChatColor.GOLD + " [PLAYER]");
        addCommandExample(ChatColor.GREEN + "/tphere" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Teleport Njodi directly to you.");
        setArgRange(1, 1);
        addKey("teleport tphere");
        addKey("tps tphere");
        addKey("tphere");
        setPermission("teleport.tphere", "Allows users to teleport other players. (/tphere)", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPHERE);
    }
}
