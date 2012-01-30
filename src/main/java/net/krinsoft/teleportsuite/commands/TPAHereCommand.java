package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPAHereCommand extends TeleportCommand {

    public TPAHereCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: TPA Here");
        setCommandUsage("/tpahere [PLAYER]");
        addCommandExample("/tpahere krinsdeath - Ask krinsdeath to teleport to you.");
        setArgRange(1, 1);
        addKey("teleport tpahere");
        addKey("tps tpahere");
        addKey("tpahere");
        setPermission("teleport.tpahere", "Allows users to request a user to teleport to them.", PermissionDefault.TRUE);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        super.runTeleport(sender, args.get(0), Request.Type.TPAHERE);
    }
}
