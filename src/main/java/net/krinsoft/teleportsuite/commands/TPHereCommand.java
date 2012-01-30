package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPHereCommand extends TeleportCommand {

    public TPHereCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TP Here");
        setCommandUsage("/tphere [PLAYER]");
        addCommandExample("/tphere krinsdeath - Teleport krinsdeath directly to you.");
        setArgRange(1, 1);
        addKey("teleport tphere");
        addKey("tps tphere");
        addKey("tphere");
        setPermission("teleport.tphere", "Allows users to teleport other players directly to them.", PermissionDefault.OP);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPHERE);
    }
}
