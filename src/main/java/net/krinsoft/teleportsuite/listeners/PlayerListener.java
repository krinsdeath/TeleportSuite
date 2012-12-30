package net.krinsoft.teleportsuite.listeners;

import net.krinsoft.teleportsuite.Request;
import net.krinsoft.teleportsuite.TeleportPlayer;
import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class PlayerListener implements Listener {
    private TeleportSuite plugin;
    private final static Pattern position = Pattern.compile("[\\[<](loc|pos)[>\\]]");
    
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

    @EventHandler(ignoreCancelled = true)
    void playerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() == null) { return; }
        Player p = event.getPlayer();
        Location l = p.getLocation();
        String loc =
                "<" + l.getWorld().getName() + "@x=" +
                Math.round(l.getX()) + ",y=" +
                Math.round(l.getY()) + ",z=" +
                Math.round(l.getZ()) + ">";
        String message = event.getMessage();
        message = position.matcher(message).replaceAll(loc);
        event.setMessage(message);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void playerTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().equals(event.getTo())) { return; }
        plugin.debug(event.getPlayer().getName() + " teleported!");
        TeleportPlayer p = plugin.getManager().getPlayer(event.getPlayer().getName());
        if (p != null) {
            p.pushToStack(event.getFrom());
            if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
                plugin.getManager().setWorldLastKnown(event.getPlayer().getName(), event.getFrom());
            }
        }
    }

    @EventHandler
    void playerDeath(PlayerDeathEvent event) {
        plugin.debug(event.getEntity().getName() + " died.");
        plugin.getManager().getPlayer(event.getEntity().getName()).pushToStack(event.getEntity().getLocation());
    }
    
}
