package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPVanillaCommand extends TeleportCommand {
    
    public TPVanillaCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Teleport");
        setCommandUsage(  ChatColor.GREEN + "/tpv" + ChatColor.GOLD + " [PLAYER]" + ChatColor.YELLOW + " [PLAYER]");
        addCommandExample(ChatColor.GREEN + "/tpv" + ChatColor.GOLD + " Njodi   " + ChatColor.YELLOW + " Player  " + ChatColor.WHITE + " -- Teleports Njodi to Player");
        setArgRange(2, 2);
        addKey("teleport vanilla");
        addKey("tps vanilla");
        addKey("tpvanilla");
        addKey("vanilla");
        addKey("tpv");
        setPermission("teleport.vanilla", "Allows users to teleport each other to other users.", PermissionDefault.OP);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        Player from = plugin.getServer().getPlayer(args.get(0));
        Player to = plugin.getServer().getPlayer(args.get(1));
        if (from == null || to == null) {
            if (from == null) {
                sender.sendMessage(plugin.getLocalization(null).get("error.invalid.player", args.get(0)));
            }
            if (to == null) {
                sender.sendMessage(plugin.getLocalization(null).get("error.invalid.player", args.get(1)));
            }
            return;
        }
        from.teleport(to);
    }
}
