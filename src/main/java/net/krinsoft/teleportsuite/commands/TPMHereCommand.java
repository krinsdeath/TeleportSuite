package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPMHereCommand extends TeleportCommand {

    public TPMHereCommand(TeleportSuite instance) {
        super(instance);
        setName("TeleportSuite: TPMHere");
        setCommandUsage("/tpmhere [player from] [player to]");
        addCommandExample("/tpmhere Njodi krinsdeath -- Equivalent to Njodi typing: /tpahere krinsdeath, without the permission check");
        setArgRange(2, 2);
        addKey("teleport tpmhere");
        addKey("tps tpmhere");
        addKey("tpmhere");
        setPermission("teleport.tpmhere", "Requests a teleport from one user to another, but bypasses typical permission checks.", PermissionDefault.FALSE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        CommandSender from = plugin.getServer().getPlayer(args.get(0));
        CommandSender to = plugin.getServer().getPlayer(args.get(1));
        if (from == null || to == null) {
            sender.sendMessage(ChatColor.RED + "Both players must exist (and be online).");
            return;
        }
        super.runTeleport(from, to.getName(), Request.Type.TPAHERE);
    }
}
