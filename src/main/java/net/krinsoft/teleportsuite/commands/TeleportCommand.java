package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.Command;
import net.krinsoft.teleportsuite.Localization;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * @author krinsdeath
 */
public abstract class TeleportCommand extends Command {
    protected TeleportSuite plugin;
    protected Localization locs;
    
    public TeleportCommand(TeleportSuite plugin) {
        super(plugin);
        
    }
    
    protected CommandSender[] check(CommandSender sender, String target) {
        CommandSender[] checked = new CommandSender[2];
        if (sender instanceof ConsoleCommandSender) {
            plugin.log("Consoles can't teleport!");
            return null;
        }
        if (target == null || plugin.getServer().getPlayer(target) == null) {
            sender.sendMessage(locs.error("invalid.player", target));
            return null;
        }
        checked[0] = sender;
        checked[1] = plugin.getServer().getPlayer(target);
        return checked;
    }

}
