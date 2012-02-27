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
public class TPSilentCommand extends TeleportCommand {

    public TPSilentCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Silent");
        setCommandUsage(  ChatColor.GREEN + "/silent");
        addCommandExample(ChatColor.GREEN + "/silent" + ChatColor.WHITE + " -- Toggles your stealth setting.");
        setArgRange(0, 0);
        addKey("teleport silent");
        addKey("tps silent");
        addKey("tpsilent");
        addKey("silent");
        addKey("tpsil");
        setPermission("teleport.silent", "Allows this user to stealth their /tpo and /tpohere commands.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (sender instanceof ConsoleCommandSender) { return; }
        TeleportPlayer p = manager.getPlayer(sender.getName());
        p.toggleSilence();
    }
}
