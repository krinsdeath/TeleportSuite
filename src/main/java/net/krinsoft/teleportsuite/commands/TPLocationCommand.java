package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPLocationCommand extends TeleportCommand {
    
    public TPLocationCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Location");
        setCommandUsage("/tploc [player]");
        setArgRange(0, 1);
        addKey("teleport location");
        addKey("tps location");
        addKey("tplocation");
        addKey("location");
        addKey("tploc");
        setPermission("teleport.location", "Allows this user to check their current location.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer player = manager.getPlayer(sender.getName());
        String[] loc = { ""+(int)player.getLocation().getX(), ""+(int)player.getLocation().getY(), ""+(int)player.getLocation().getY() };
        player.sendMessage("World: " + player.getLocation().getWorld().getName() + " / " + player.getLocation().getWorld().getEnvironment().name());
        player.sendMessage("X: " + loc[0]);
        player.sendMessage("Y: " + loc[1]);
        player.sendMessage("Z: " + loc[2]);
    }
}
