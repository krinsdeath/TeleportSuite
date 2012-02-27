package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class LocalizationCommand extends TeleportCommand {

    public LocalizationCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Language");
        setCommandUsage(  ChatColor.GREEN + "/tplang" + ChatColor.GOLD + " [language]");
        addCommandExample(ChatColor.GREEN + "/tplang" + ChatColor.GOLD + " english");
        setArgRange(0, 1);
        addKey("teleport lang");
        addKey("tps lang");
        addKey("tplang");
        setPermission("teleport.language", "Sets the user's localized language.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer player = manager.getPlayer(sender.getName());
        List<String> languages = plugin.getConfig().getStringList("languages.available");
        if (args.size() == 0) {
            player.sendLocalizedString("default.languages", null);
            for (String language : languages) {
                String name = plugin.getLocalization(language).get("default.name", null);
                player.sendMessage(language + ": " + name);
            }
        } else {
            if (languages.contains(args.get(0))) {
                player.setLanguage(args.get(0));
            } else {
                player.sendLocalizedString("error.invalid.language", "&CThere is no such language available.");
            }
        }
    }
}
