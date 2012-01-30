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
public class TPRewindCommand extends TeleportCommand {
    
    public TPRewindCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Rewind");
        setCommandUsage("/tprewind [target] [num]");
        addCommandExample("/tprewind 3 -- Rewinds you three of your last 5 destinations.");
        addCommandExample("/tprewind krinsdeath 2 -- Rewinds krinsdeath through 2 of his last 5 destinations.");
        setArgRange(1, 2);
        addKey("teleport rewind");
        addKey("tps rewind");
        addKey("tprewind");
        addKey("rewind");
        addKey("tprew");
        setPermission("teleport.rewind", "Rewinds through previous locations for the specified target.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        String target = sender.getName();
        int stack = 1;
        try {
            if (args.size() == 1) {
                stack = Integer.parseInt(args.get(0));
            } else {
                stack = Integer.parseInt(args.get(1));
            }
        } catch (NumberFormatException e) {
            target = args.get(0);
        }
        if (target.equalsIgnoreCase(sender.getName()) && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(plugin.getLocalization(null).get("error.invalid.arguments", null));
            return;
        }
        TeleportPlayer p = manager.getPlayer(target);
        p.rewind(stack);
    }
}
