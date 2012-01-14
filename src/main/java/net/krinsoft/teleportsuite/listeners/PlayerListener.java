package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.TeleportSuite;

/**
 * @author krinsdeath
 */
public class PlayerListener extends org.bukkit.event.player.PlayerListener {
    private TeleportSuite plugin;
    
    public PlayerListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }
    
}
