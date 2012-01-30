package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPOCommand extends TeleportCommand {

    public TPOCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TPO");
        setCommandUsage("/tpo [PLAYER]");
        addCommandExample("/tpo krinsdeath - Teleport directly to krinsdeath, overriding any settings.");
        setArgRange(1, 1);
        addKey("teleport tpo");
        addKey("tps tpo");
        addKey("tpo");
        setPermission("teleport.tpo", "Teleports you directly to another user, bypassing their toggle setting.", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPO);
    }
}
