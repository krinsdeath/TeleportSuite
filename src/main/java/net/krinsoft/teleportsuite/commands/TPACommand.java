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
public class TPACommand extends TeleportCommand {
    
    public TPACommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TPA");
        setCommandUsage(  ChatColor.GREEN + "/tpa" + ChatColor.GOLD + " [PLAYER]");
        addCommandExample(ChatColor.GREEN + "/tpa" + ChatColor.GOLD + " Njodi   " + ChatColor.WHITE + " -- Ask Njodi if you can teleport to him.");
        setArgRange(1, 1);
        addKey("teleport tpa");
        addKey("tps tpa");
        addKey("tpa");
        setPermission("teleport.tpa", "Allows user to request a teleport with /tpa", PermissionDefault.TRUE);
    }
    
    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPA);
    }
}
