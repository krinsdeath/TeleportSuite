package net.krinsoft.teleportsuite.destinations;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class TPDestination {
    private TeleportSuite plugin;
    private Location location;

    public TPDestination(TeleportSuite plugin, String world, String loc) {
        this.plugin = plugin;
        String[] locs = loc.split(":");
        try {
            location = new Location(plugin.getServer().getWorld(world), Double.parseDouble(locs[0]), Double.parseDouble(locs[1])+0.1, Double.parseDouble(locs[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NumberFormatException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NullPointerException e) {
            plugin.debug("An error occurred while parsing a location string.");
        }
    }
    
    public TPDestination(TeleportSuite plugin, Location loc) {
        this.plugin = plugin;
        location = loc;
        location.setY(location.getY()+0.1);
    }

    public Location getLocation() {
        return location;
    }
    
    public void setDestination(Location loc) {
        location = loc;
    }
    
    public void setDestination(String loc) {
        String[] locs = loc.split(":");
        try {
            String world = locs[0];
            location = new Location(plugin.getServer().getWorld(world), Double.parseDouble(locs[1]), Double.parseDouble(locs[2]), Double.parseDouble(locs[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            plugin.warn("An error occurred while parsing a location string.");
        } catch (NumberFormatException e) {
            plugin.warn("An error occurred while parsing a location string.");
        } catch (NullPointerException e) {
            plugin.warn("An error occurred while parsing a location string.");
        }
    }

}
