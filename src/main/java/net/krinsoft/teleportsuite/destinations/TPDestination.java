package net.krinsoft.teleportsuite.destinations;

import net.krinsoft.teleportsuite.TeleportSuite;
import org.bukkit.Location;

/**
 * @author krinsdeath
 */
@SuppressWarnings("unused")
public class TPDestination {
    private TeleportSuite plugin;
    private String world;
    private double x;
    private double y;
    private double z;

    public TPDestination(TeleportSuite plugin, String world, String loc) {
        this.plugin = plugin;
        String[] locs = loc.split(":");
        try {
            this.world = world;
            this.x = Double.parseDouble(locs[0]);
            this.y = Double.parseDouble(locs[1])+0.1;
            this.z = Double.parseDouble(locs[2]);
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
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY()+0.1;
        this.z = loc.getZ();
    }

    public Location getLocation() {
        return new Location(plugin.getServer().getWorld(world), x, y, z);
    }
    
    public void setDestination(Location loc) {
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
    }
    
    public void setDestination(String loc) {
        String[] locs = loc.split(":");
        try {
            this.world = locs[0];
            this.x = Double.parseDouble(locs[1]);
            this.y = Double.parseDouble(locs[2]);
            this.z = Double.parseDouble(locs[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            plugin.warn("An error occurred while parsing a location string.");
        } catch (NumberFormatException e) {
            plugin.warn("An error occurred while parsing a location string.");
        } catch (NullPointerException e) {
            plugin.warn("An error occurred while parsing a location string.");
        }
    }

    @Override
    public String toString() {
        return "TPDestination{world=" + world + ",x=" + x + ",y=" + y +",z=" + z +"}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = hash * 17 + toString().hashCode();
        return hash * 3;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (o.getClass() != this.getClass()) { return false; }
        TPDestination that = (TPDestination) o;
        return (that.x == this.x && that.y == this.y && that.z == this.z && that.world.equals(this.world));
    }

}
