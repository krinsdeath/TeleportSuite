package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.Command;
import net.krinsoft.teleportsuite.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author krinsdeath
 */
public abstract class TeleportCommand extends Command {
    protected TeleportSuite plugin;
    protected TeleportManager manager;
    protected Localization locs;
    
    protected int curr;
    private boolean economy = false;
    
    public TeleportCommand(TeleportSuite plugin) {
        super(plugin);
        this.plugin = plugin;
        this.manager = plugin.getManager();
        this.economy = plugin.getBank() != null;
        this.curr = plugin.getConfig().getInt("economy.currency", -1);
    }

    protected CommandSender[] check(CommandSender sender, String target) {
        CommandSender[] checked = new CommandSender[2];
        if (sender instanceof ConsoleCommandSender) {
            plugin.log("Consoles can't teleport!");
            return null;
        }
        if (target == null || plugin.getServer().getPlayer(target) == null) {
            manager.getPlayer(sender.getName()).sendLocalizedString("error.invalid.player", target);
            return null;
        }
        checked[0] = sender;
        checked[1] = plugin.getServer().getPlayer(target);
        return checked;
    }
    
    protected boolean verifyWallet(CommandSender sender, String type) {
        Player player = plugin.getServer().getPlayer(sender.getName());
        if (player == null) { return false; }
        if (!economy) { return true; }
        double amount = plugin.getConfig().getDouble("economy." + type, 0);
        if (plugin.getBank().hasEnough(player, amount, curr)) {
            plugin.debug("'" + player.getName() + "' has enough currency.");
            return true;
        } else {
            plugin.debug("'" + player.getName() + "' doesn't have enough currency.");
            return false;
        }
    }
    
    protected void runTeleport(CommandSender sender, String arg, Request.Type type) {
        if (!verifyWallet(sender, type.getName())) { return; }
        CommandSender[] checked = check(sender, arg);
        if (checked == null) { return; }
        TeleportPlayer from = manager.getPlayer(checked[0].getName());
        TeleportPlayer to = manager.getPlayer(checked[1].getName());
        manager.queue(from, to, type);
    }

}
