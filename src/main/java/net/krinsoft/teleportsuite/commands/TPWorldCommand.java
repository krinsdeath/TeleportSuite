package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPWorldCommand extends TeleportCommand {
    
    public TPWorldCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TP World");
        setCommandUsage("/tpworld [WORLD] [-s]");
        addCommandExample("/tpworld world -- Teleports you to your last location in 'world'");
        addCommandExample("/tpworld world -s -- Teleports you to the 'world' spawn point.");
        addCommandExample("/tpworld -s -- Teleport to your current world's spawn point.");
        setArgRange(1, 2);
        addKey("teleport world");
        addKey("tps world");
        addKey("tpworld");
        addKey("world");
        addKey("tpw");
        setPermission("teleport.world", "Allows users to teleport to the specified world.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!verifyWallet(sender, "tpworld")) { return; }
        String world = args.get(0);
        boolean spawn = false;
        TeleportPlayer p = manager.getPlayer(sender.getName());
        if (args.get(0).equals("-s")) {
            world = p.getLocation().getWorld().getName();
            spawn = true;
        }
        if (args.size() == 2) {
            if (plugin.getServer().getWorld(args.get(0)) != null) {
                world = args.get(0);
            } else {
                world = p.getLocation().getWorld().getName();
            }
            if (args.get(1).equals("-s")) {
                spawn = true;
            }
        }
        manager.tpworld(p, world, spawn);
    }

}
