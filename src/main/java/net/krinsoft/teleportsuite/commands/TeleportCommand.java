package net.krinsoft.teleportsuite.commands;

import com.pneumaticraft.commandhandler.Command;
import net.krinsoft.teleportsuite.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author krinsdeath
 */
public abstract class TeleportCommand extends Command {
    protected TeleportSuite plugin;
    protected TeleportManager manager;

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
        if (!economy || sender.hasPermission("teleport.economy.bypass")) { return true; }
        double amount = plugin.getConfig().getDouble("economy." + type, 0);
        if (plugin.getBank().hasEnough(player, amount, curr)) {
            plugin.debug("'" + player.getName() + "' has enough currency.");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Your pockets aren't deep enough for that.");
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
        if (from.equals(to)) {
            from.sendLocalizedString("error.invalid.target", checked[1].getName());
        }
        manager.queue(from, to, type);
    }

    @Override
    public void showHelp(CommandSender sender) {
        showHeader(sender);
        showUsage(sender);
        showDescription(sender);
        showPermission(sender);
        showAliases(sender);
        showExamples(sender);
    }

    public void showHeader(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "=== " + ChatColor.AQUA + getCommandName() + ChatColor.GREEN + " ===");
    }

    public void showUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Usage:       " + ChatColor.AQUA + getCommandUsage());
    }

    public void showDescription(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Description: " + ChatColor.GOLD + getCommandDesc());
    }

    public void showPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Permission:  " + ChatColor.GOLD + this.getPermissionString());
    }

    public void showAliases(CommandSender sender) {
        String keys = "";
        for (String key : this.getKeyStrings()) {
            keys += key + ", ";
        }
        keys = keys.substring(0, keys.length() - 2);
        sender.sendMessage(ChatColor.GREEN + "Aliases: " + ChatColor.RED + keys);
    }

    public void showExamples(CommandSender sender) {
        if (this.getCommandExamples().size() > 0) {
            sender.sendMessage(ChatColor.GREEN + "Examples: ");
            if (sender instanceof Player) {
                for (int i = 0; i < 4 && i < this.getCommandExamples().size(); i++) {
                    sender.sendMessage(this.getCommandExamples().get(i));
                }
            } else {
                for (String c : this.getCommandExamples()) {
                    sender.sendMessage(c);
                }
            }
        }
    }
}
