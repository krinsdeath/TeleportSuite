package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.TeleportSuite;

/**
 * @author krinsdeath
 */
public class EntityListener extends org.bukkit.event.entity.EntityListener {
    private TeleportSuite plugin;
    
    public EntityListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }


}
