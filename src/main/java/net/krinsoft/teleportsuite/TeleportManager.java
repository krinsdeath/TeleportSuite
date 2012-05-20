package net.krinsoft.teleportsuite;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krinsdeath
 */
public class TeleportManager {
    private TeleportSuite plugin;
    private Map<String, TeleportPlayer> players = new HashMap<String, TeleportPlayer>();

    public TeleportManager(TeleportSuite plugin) {
        players.clear();
        this.plugin = plugin;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            register(p.getName());
        }
    }
    
    public void disable() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            unregister(p.getName());
        }
        players.clear();
        plugin.saveUsers();
    }

    public TeleportPlayer register(String player) {
        if (!players.containsKey(player)) {
            players.put(player, new TeleportPlayer(plugin, player));
            plugin.debug(player + " was registered.");
        }
        return players.get(player);
    }
    
    public TeleportPlayer unregister(String player) {
        TeleportPlayer p = players.remove(player);
        if (p != null) {
            p.save();
            p.clean();
            plugin.debug(p.getName() + " was unregistered.");
        } else {
            plugin.debug(player + " was not registered; couldn't unregister...");
        }
        return p;
    }
    
    public TeleportPlayer getPlayer(String player) {
        register(player);
        return players.get(player);
    }

    public void queue(TeleportPlayer from, TeleportPlayer to, Request.Type type) {
        String node = "teleport.world." + to.getLocation().getWorld().getName();
        if (!from.hasPermission(node)) {
            return;
        }
        if (from.equals(to)) { return; }
        if (type == Request.Type.TPA || type == Request.Type.TPAHERE) {
            if (from.isRequesting()) { return; }
            Request req = new Request(from, type);
            if (!to.getRequests().contains(req)) {
                if (to.getStatus() == TeleportPlayer.Status.REJECTING) {
                    from.sendLocalizedString("error.requests.ignored", to.getName());
                    return;
                } else if (to.getStatus() == TeleportPlayer.Status.AUTO) {
                    switch (type) {
                        case TPA: executeTPA(from, to); break;
                        case TPAHERE: executeTPA(to, from); break;
                    }
                    deductFunds(from, req.getType().getName());
                    return;
                }
                from.sendLocalizedString("teleport.tprequest", to.getName());
                switch (type) {
                    case TPA : to.sendLocalizedString("teleport.requests.tpa", from.getName()); break;
                    case TPAHERE : to.sendLocalizedString("teleport.requests.tpahere", from.getName()); break;
                }
                from.setRequesting(to.getName(), true);
                to.getRequests().add(req);
            }
        } else {
            if (to.getStatus() == TeleportPlayer.Status.REJECTING && (type == Request.Type.TP || type == Request.Type.TPHERE)) {
                from.sendLocalizedString("error.requests.ignored", to.getName());
                return;
            }
            switch (type) {
                // from -> to
                case TP: executeTP(from, to); break;
                case TPO: executeTPO(from, to); break;
                // to -> from
                case TPHERE: executeTP(to, from); break;
                case TPOHERE: executeTPO(to, from); break;
                // default?
                default: return;
            }
            deductFunds(from, type.getName());
        }
    }

    public void finish(TeleportPlayer from, Request request, boolean val) {
        if (request == null) {
            from.sendLocalizedString("error.invalid.player", null);
            return;
        }
        if (val) {
            from.sendLocalizedString("teleport.tpaccept", request.getTo().getName());
            switch (request.getType()) {
                // from -> to
                case TPAHERE: executeTPA(from, request.getTo()); break;
                case TPHERE: executeTP(from, request.getTo()); break;
                case TPOHERE: executeTPO(from, request.getTo()); break;
                // to -> from
                case TPA: executeTPA(request.getTo(), from); break;
                case TP: executeTP(request.getTo(), from); break;
                case TPO: executeTPO(request.getTo(), from); break;
            }
            deductFunds(request.getTo(), request.getType().getName());
        } else {
            from.sendLocalizedString("teleport.tpreject", request.getTo().getName());
            request.getTo().sendLocalizedString("error.requests.rejected", from.getName());
        }
        from.getRequests().remove(request);
        request.getTo().setRequesting(null, false);
    }
    
    private void executeTPA(TeleportPlayer from, TeleportPlayer to) {
        to.sendLocalizedString("teleport.tpa", from.getName());
        from.sendLocalizedString("teleport.tpahere", to.getName());
        teleport(from, to);
    }
    
    private void executeTP(TeleportPlayer from, TeleportPlayer to) {
        to.sendLocalizedString("teleport.tp", from.getName());
        from.sendLocalizedString("teleport.tphere", to.getName());
        teleport(from, to);
    }
    
    private void executeTPO(TeleportPlayer from, TeleportPlayer to) {
        if (!from.isSilent()) {
            to.sendLocalizedString("teleport.tpo", from.getName());
        }
        from.sendLocalizedString("teleport.tpohere", to.getName());
        teleport(from, to);
    }
    
    private void teleport(TeleportPlayer from, TeleportPlayer to) {
        from.teleport(to.getLocation());
    }
    
    public boolean tpworld(TeleportPlayer player, String world, boolean spawn) {
        if (plugin.getServer().getWorld(world) == null) {
            player.sendLocalizedString("error.invalid.world", world);
            return false;
        }
        String node = "teleport.world." + world;
        if (!player.hasPermission(node)) {
            return false;
        }
        setWorldLastKnown(player.getName(), player.getLocation());
        if (!spawn) {
            player.teleport(getWorldLastKnown(player.getName(), world));
        } else {
            player.teleport(plugin.getServer().getWorld(world).getSpawnLocation());
        }
        player.sendLocalizedString("teleport.tpworld", world);
        deductFunds(player, "tpworld");
        return true;
    }
    
    private void deductFunds(TeleportPlayer p, String key) {
        if (plugin.getBank() != null) {
            double amount = plugin.getConfig().getDouble("economy." + key);
            int curr = plugin.getConfig().getInt("economy.currency");
            plugin.getBank().take(p.getReference(), amount, curr);
        }
    }
    
    public void setWorldLastKnown(String player, Location l) {
        String location = createLocationString(l);
        plugin.getUsers().set(player + "." + l.getWorld().getName(), location);
        plugin.saveUsers();
    }
    
    public Location getWorldLastKnown(String player, String world) {
        Location location = plugin.getServer().getWorld(world).getSpawnLocation();
        world = location.getWorld().getName();
        if (plugin.getUsers().get(player + "." + world) == null) {
            return location;
        }
        String last = plugin.getUsers().getString(player + "." + world);
        double[] xyz = new double[3];
        try {
            String[] loc = last.split(":");
            xyz[0] = Double.parseDouble(loc[0]);
            xyz[1] = Double.parseDouble(loc[1])+0.15;
            xyz[2] = Double.parseDouble(loc[2]);
            location = new Location(plugin.getServer().getWorld(world), xyz[0], xyz[1], xyz[2]);
            if (location.getWorld().getBlockAt((int)location.getX(), (int)location.getY() + 1, (int)location.getZ()).getType() != Material.AIR) {
                location = location.getWorld().getHighestBlockAt((int)location.getX(), (int)location.getZ()).getLocation();
                location.setY(location.getY()+1);
            }
        } catch (NumberFormatException e) {
            plugin.debug("An error occurred while parsing a location in 'users.yml'");
        } catch (IndexOutOfBoundsException e) {
            plugin.debug("An error occurred while parsing a location in 'users.yml'");
        }
        return location;
    }
    
    public String createLocationString(Location l) {
        double[] xyz = new double[3];
        xyz[0] = l.getX();
        xyz[1] = l.getY();
        xyz[2] = l.getZ();
        return xyz[0] + ":" + xyz[1] + ":" + xyz[2];
    }
}
