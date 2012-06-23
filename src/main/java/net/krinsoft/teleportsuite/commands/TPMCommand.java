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
public class TPMCommand extends TeleportCommand {

    public TPMCommand(TeleportSuite instance) {
        super(instance);
        setName("TeleportSuite: TPM");
        setCommandUsage("/tpm [player from] [player to]");
        addCommandExample("/tpm Njodi krinsdeath -- Equivalent to Njodi typing: /tpa krinsdeath, without the permission check");
        setArgRange(2, 2);
        addKey("teleport tpm");
        addKey("tps tpm");
        addKey("tpm");
        setPermission("teleport.tpm", "Requests a teleport from one user to another, but bypasses typical permission checks.", PermissionDefault.FALSE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        CommandSender from = plugin.getServer().getPlayer(args.get(0));
        CommandSender to = plugin.getServer().getPlayer(args.get(1));
        if (from == null || to == null) {
            sender.sendMessage(ChatColor.RED + "Both players must exist (and be online).");
            return;
        }
        super.runTeleport(from, to.getName(), Request.Type.TPA);
    }
}
