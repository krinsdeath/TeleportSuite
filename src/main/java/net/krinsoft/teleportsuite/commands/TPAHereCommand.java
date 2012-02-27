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
public class TPAHereCommand extends TeleportCommand {

    public TPAHereCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TPA Here");
        setCommandUsage(  ChatColor.GREEN + "/tpahere" + ChatColor.GOLD + " [PLAYER]");
        addCommandExample(ChatColor.GREEN + "/tpahere" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Ask Njodi to teleport to you.");
        setArgRange(1, 1);
        addKey("teleport tpahere");
        addKey("tps tpahere");
        addKey("tpahere");
        setPermission("teleport.tpahere", "Allows users to request a teleport with /tpahere.", PermissionDefault.TRUE);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPAHERE);
    }
}
