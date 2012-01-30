package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

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
        TeleportPlayer p = plugin.getManager().getPlayer(event.getPlayer().getName());
        for (Request r : new ArrayList<Request>(p.getRequests())) {
            plugin.getManager().finish(p, r, false);
        }
        plugin.getManager().unregister(event.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    void playerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) { return; }
        if (event.getFrom().equals(event.getTo())) { return; }
        plugin.getManager().getPlayer(event.getPlayer().getName()).pushToStack(event.getFrom());
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            plugin.getManager().setWorldLastKnown(event.getPlayer().getName(), event.getFrom());
        }
    }
    
}
