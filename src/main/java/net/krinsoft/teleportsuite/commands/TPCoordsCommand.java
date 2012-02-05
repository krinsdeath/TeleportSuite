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
public class TPCoordsCommand extends TeleportCommand {
    
    public TPCoordsCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Coords");
        setCommandUsage("/tpcoords");
        setArgRange(0, 0);
        addKey("teleport coords");
        addKey("tps coords");
        addKey("tpcoords");
        addKey("coords");
        addKey("tpco");
        setPermission("teleport.coords", "Allows this user to check their location.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> strings) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer player = manager.getPlayer(sender.getName());
        String[] loc = { ""+(int)player.getLocation().getX(), ""+(int)player.getLocation().getY(), ""+(int)player.getLocation().getY() };
        player.sendMessage("World: " + player.getLocation().getWorld().getName() + " / " + player.getLocation().getWorld().getEnvironment().name());
        player.sendMessage("X: " + loc[0]);
        player.sendMessage("Y: " + loc[1]);
        player.sendMessage("Z: " + loc[2]);    }
}
