package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;
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
        Location l = player.getLocation();
        player.sendMessage("World: " + l.getWorld().getName() + " / " + l.getWorld().getEnvironment().name());
        player.sendMessage("X: " + Math.floor(l.getX()));
        player.sendMessage("Y: " + Math.floor(l.getY()));
        player.sendMessage("Z: " + Math.floor(l.getZ()));
    }
}
