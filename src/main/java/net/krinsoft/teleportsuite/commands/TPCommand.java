package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPCommand extends TeleportCommand {

    public TPCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TP");
        setCommandUsage(  ChatColor.GREEN + "/tp" + ChatColor.GOLD + " [PLAYER]" + ChatColor.YELLOW + " [PLAYER] ");
        addCommandExample(ChatColor.GREEN + "/tp" + ChatColor.GOLD + " Njodi   " + ChatColor.YELLOW + "          " + ChatColor.WHITE + " -- Teleport directly to Njodi.");
        addCommandExample(ChatColor.GREEN + "/tp" + ChatColor.GOLD + " Njodi   " + ChatColor.YELLOW + " Player2  " + ChatColor.WHITE + " -- Teleport Njodi to Player2");
        setArgRange(1, 2);
        addKey("teleport tp");
        addKey("tps tp");
        addKey("tp");
        setPermission("teleport.tp", "Allows users to teleport. (/tp)", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender && args.size() < 2) {
            sender.sendMessage(plugin.getLocalization(null).get("error.invalid.arguments", null));
            return;
        }
        CommandSender from = sender;
        String to = args.get(0);
        if (args.size() == 2 && plugin.getServer().getPlayer(args.get(0)) != null) {
            if (!sender.hasPermission("teleport.tp.other")) {
                if (!(sender instanceof ConsoleCommandSender)) {
                    plugin.getManager().getPlayer(sender.getName()).sendLocalizedString("error.permission", "teleport.tp.other");
                    return;
                }
            }
            from = plugin.getServer().getPlayer(args.get(0));
            to = args.get(1);
        }
        super.runTeleport(from, to, Request.Type.TP);
    }
}
