package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
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
        setCommandUsage("/tp [PLAYER]");
        addCommandExample("/tp krinsdeath - Teleport directly to krinsdeath.");
        setArgRange(1, 2);
        addKey("teleport tp");
        addKey("tps tp");
        addKey("tp");
        setPermission("teleport.tp", "Teleports directly to another player.", PermissionDefault.OP);
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
