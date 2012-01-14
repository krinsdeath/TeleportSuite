package net.krinsoft.teleportsuite.destinations;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;

/**
 * @author krinsdeath
 */
public class TPDestination {
    private Location location;

    public TPDestination(TeleportSuite plugin, String world, String loc) {
        String[] locs = loc.split(":");
        try {
            location = new Location(plugin.getServer().getWorld(world), Double.parseDouble(locs[0]), Double.parseDouble(locs[1]), Double.parseDouble(locs[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NumberFormatException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NullPointerException e) {
            plugin.debug("An error occurred while parsing a location string.");
        }
    }
    
    public TPDestination(Location loc) {
        location = loc;
    }

    public Location getLocation() {
        return location;
    }
    
    public void setDestination(Location loc) {
        location = loc;
    }
    
    public void setDestination(TeleportSuite plugin, String loc) {
        String[] locs = loc.split(":");
        try {
            String world = locs[0];
            location = new Location(plugin.getServer().getWorld(world), Double.parseDouble(locs[1]), Double.parseDouble(locs[2]), Double.parseDouble(locs[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NumberFormatException e) {
            plugin.debug("An error occurred while parsing a location string.");
        } catch (NullPointerException e) {
            plugin.debug("An error occurred while parsing a location string.");
        }
    }

}
