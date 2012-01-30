package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPCommand extends TeleportCommand {

    public TPCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TP");
        setCommandUsage("/tp [PLAYER]");
        addCommandExample("/tp krinsdeath - Teleport directly to krinsdeath.");
        setArgRange(1, 2);
        addKey("teleport tp");
        addKey("tps tp");
        addKey("tp");
        setPermission("teleport.tp", "Teleports directly to another player.", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        CommandSender from = sender;
        String to = args.get(0);
        if (args.size() == 2 && plugin.getServer().getPlayer(args.get(0)) != null) {
            from = plugin.getServer().getPlayer(args.get(0));
            to = args.get(1);
        }
        super.runTeleport(from, to, Request.Type.TP);
    }
}
