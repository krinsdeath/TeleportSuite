package net.krinsoft.teleportsuite.commands;

import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * @author krinsdeath
 */
public class TPCancelCommand extends TeleportCommand {
    
    public TPCancelCommand(TeleportSuite plugin) {
        super(plugin);
        setName("TeleportSuite: Cancel");
        setCommandUsage("/tpcancel");
        setArgRange(0, 0);
        addKey("teleport cancel");
        addKey("tps cancel");
        addKey("tpcancel");
        addKey("cancel");
        addKey("tpcan");
        setPermission("teleport.cancel", "Allows the user to cancel their requests.", PermissionDefault.TRUE);
    }
    
    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        TeleportPlayer p = manager.getPlayer(sender.getName());
        p.cancelRequest();
    }
}
