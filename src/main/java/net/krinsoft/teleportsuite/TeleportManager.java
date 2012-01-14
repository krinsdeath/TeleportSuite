package net.krinsoft.teleportsuite;

import org.bukkit.entity.Player;

/**
 * @author krinsdeath
 */
public class TeleportManager {
    private TeleportSuite plugin;
    
    public TeleportManager(TeleportSuite plugin) {
        this.plugin = plugin;
    }

    /*
     * Attempts to teleport the player 'from' to the 'target'
     * @return
     * true on success (player successfully teleported
     * false on failure (player ignoring teleports or doesn't exist)
     */
    public boolean tpa(TeleportPlayer from, TeleportPlayer target) {
        return false;
    }
    
}
