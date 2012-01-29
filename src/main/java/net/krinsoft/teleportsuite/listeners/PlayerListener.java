package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class PlayerListener implements Listener {
    private TeleportSuite plugin;
    
    public PlayerListener(TeleportSuite plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void playerJoin(PlayerJoinEvent event) {
        plugin.getManager().register(event.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void playerQuit(PlayerQuitEvent event) {
        plugin.getManager().unregister(event.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void playerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getFrom().equals(event.getTo())) { return; }
        plugin.getManager().getPlayer(event.getPlayer().getName()).pushToStack(event.getTo());
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            plugin.getManager().setWorldLastKnown(event.getPlayer().getName(), event.getFrom());
        }
    }
    
}
