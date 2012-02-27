package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author krinsdeath
 */
public class BaseCommand extends TeleportCommand {

    private Map<Integer, List<String>> pages = new HashMap<Integer, List<String>>();

    public BaseCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Main");
        setCommandUsage(ChatColor.GREEN + "/tps");
        setArgRange(0, 1);
        addToPage(0, ChatColor.GREEN + "Type any command, followed by a '" + ChatColor.AQUA + "?" + ChatColor.GREEN + "' to see command usage.");
        addToPage(0, ChatColor.GREEN + "/tps" + ChatColor.GOLD + " [page]" + ChatColor.WHITE + " -- Show paged help.");
        addToPage(1, ChatColor.GOLD + "### Administrative Commands " + ChatColor.AQUA + "[Page 1]" + ChatColor.GOLD + " ###");
        addToPage(1, ChatColor.GREEN + "/tps" + ChatColor.GOLD + " reload");
        addToPage(1, ChatColor.GREEN + "/tps" + ChatColor.GOLD + " debug");
        addToPage(2, ChatColor.GOLD + "### Teleportation Commands " + ChatColor.AQUA + "[Page 2]" + ChatColor.GOLD + " ###");
        addToPage(2, ChatColor.GREEN + "/tpa     " + ChatColor.GOLD + " [player]");
        addToPage(2, ChatColor.GREEN + "/tpahere " + ChatColor.GOLD + " [player]");
        addToPage(2, ChatColor.GREEN + "/tp      " + ChatColor.GOLD + " [player]" + ChatColor.YELLOW + " [player]");
        addToPage(2, ChatColor.GREEN + "/tphere  " + ChatColor.GOLD + " [player]");
        addToPage(3, ChatColor.GOLD + "### Teleportation Commands " + ChatColor.AQUA + "[Page 3]" + ChatColor.GOLD + " ###");
        addToPage(3, ChatColor.GREEN + "/tpo     " + ChatColor.GOLD + " [player]");
        addToPage(3, ChatColor.GREEN + "/tpohere " + ChatColor.GOLD + " [player]");
        addToPage(3, ChatColor.GREEN + "/tpv     " + ChatColor.GOLD + " [player]" + ChatColor.YELLOW + " [player]");
        addToPage(3, ChatColor.GREEN + "/world   " + ChatColor.GOLD + " [world] " + ChatColor.YELLOW + " [-s]");
        addToPage(3, ChatColor.GREEN + "/loc     " + ChatColor.GOLD + " [x y z] " + ChatColor.YELLOW + " [world]");
        addToPage(4, ChatColor.GOLD + "### User Commands " + ChatColor.AQUA + "[Page 4]" + ChatColor.GOLD + " ###");
        addToPage(4, ChatColor.GREEN + "/silent  ");
        addToPage(4, ChatColor.GREEN + "/coords  ");
        addToPage(4, ChatColor.GREEN + "/requests");
        addToPage(5, ChatColor.GOLD + "### User Commands " + ChatColor.AQUA + "[Page 5]" + ChatColor.GOLD + " ###");
        addToPage(5, ChatColor.GREEN + "/accept  " + ChatColor.GOLD + " [player]");
        addToPage(5, ChatColor.GREEN + "/reject  " + ChatColor.GOLD + " [player]");
        addToPage(5, ChatColor.GREEN + "/toggle  " + ChatColor.GOLD + " [option]");
        addToPage(5, ChatColor.GREEN + "/cancel  ");
        addKey("teleport");
        addKey("tps");
        setPermission("teleport.help", "TeleportSuite basic help!", PermissionDefault.TRUE);
    }

    public void addToPage(int page, String line) {
        addCommandExample(line);
        List<String> lines = pages.get(page);
        if (lines == null) {
            lines = new ArrayList<String>();
        }
        lines.add(line);
        pages.put(page, lines);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) {
            super.showHelp(sender);
            return;
        }
        if (args.size() == 0) {
            showHelp(sender, 0);
        } else {
            try {
                showHelp(sender, Integer.parseInt(args.get(0)));
            } catch (Exception e) {
                showHelp(sender, 0);
            }
        }
    }

    public void showHelp(CommandSender sender, int page) {
        if (page >= pages.size()) { page = 0; }
        showHeader(sender, page);
        showDescription(sender);
        showPage(sender, page);
    }

    public void showHeader(CommandSender sender, int page) {
        String thePage = ChatColor.GOLD + "Page " + page + "/" + (pages.size()-1) + ChatColor.GREEN;
        sender.sendMessage(ChatColor.GREEN + "=== " + ChatColor.AQUA + getCommandName() + ChatColor.GREEN + " [" + thePage + "] ===");
    }

    public void showPage(CommandSender sender, int page) {
        List<String> lines = new ArrayList<String>();
        lines.addAll(pages.get(page));
        for (String line : lines) {
            sender.sendMessage(line);
        }
    }
}
