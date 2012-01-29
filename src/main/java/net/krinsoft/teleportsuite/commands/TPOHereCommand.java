package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPOHereCommand extends TeleportCommand {

    public TPOHereCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TPO Here");
        setCommandUsage("/tpohere [PLAYER]");
        addCommandExample("/tpohere krinsdeath - Teleport krinsdeath directly to you, overriding any settings.");
        setArgRange(1, 1);
        addKey("teleport tpohere");
        addKey("tps tpohere");
        addKey("tpohere");
        setPermission("teleport.tpohere", "Teleports another user directly to you, bypassing their toggle setting.", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args, Request.Type.TPOHERE);
    }
}
