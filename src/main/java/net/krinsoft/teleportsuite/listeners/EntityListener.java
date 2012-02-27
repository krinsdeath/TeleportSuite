package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class EntityListener implements Listener {
    private TeleportSuite plugin;
    
    public EntityListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void entityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            plugin.debug(((Player)event.getEntity()).getName() + " died!");
            plugin.getManager().getPlayer(((Player)event.getEntity()).getName()).pushToStack(event.getEntity().getLocation());
        }
    }
    
}
