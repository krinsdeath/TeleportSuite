package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
        setCommandUsage(  ChatColor.GREEN + "/coords");
        addCommandExample(ChatColor.GREEN + "/coords" + ChatColor.WHITE + " -- Show your current location.");
        setArgRange(0, 0);
        addKey("teleport coords");
        addKey("tps coords");
        addKey("tpcoords");
        addKey("coords");
        addKey("tpco");
        setPermission("teleport.coords", "Allows this user to check their location. (/coords)", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> strings) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer player = manager.getPlayer(sender.getName());
        Location l = player.getLocation();
        player.sendMessage(ChatColor.GREEN + "World: " + colorWorld(l.getWorld()) + " / " + colorEnvironment(l.getWorld().getEnvironment()));
        player.sendMessage(ChatColor.GREEN + "X: " + ChatColor.WHITE + Math.floor(l.getX()));
        player.sendMessage(ChatColor.GREEN + "Y: " + ChatColor.WHITE + Math.floor(l.getY()));
        player.sendMessage(ChatColor.GREEN + "Z: " + ChatColor.WHITE + Math.floor(l.getZ()));
    }

    public String colorWorld(World world) {
        switch (world.getEnvironment()) {
            case NORMAL:  return world.getName();
            case NETHER:  return ChatColor.RED  + world.getName() + ChatColor.WHITE;
            case THE_END: return ChatColor.AQUA + world.getName() + ChatColor.WHITE;
            default:      return world.getName();
        }
    }

    public String colorEnvironment(World.Environment env) {
        switch (env) {
            case NORMAL:  return env.name();
            case NETHER:  return ChatColor.RED  + env.name() + ChatColor.WHITE;
            case THE_END: return ChatColor.AQUA + env.name() + ChatColor.WHITE;
            default:      return env.name();
        }
    }
}
