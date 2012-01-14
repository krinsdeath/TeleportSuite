package net.krinsoft.teleportsuite;

import net.krinsoft.teleportsuite.destinations.TPDestination;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author krinsdeath
 */
public class TeleportPlayer {
    private Player ref;
    private List<TPDestination> stack = new ArrayList<TPDestination>();
    
    public TeleportPlayer(TeleportSuite plugin, String name) {
        ref = plugin.getServer().getPlayer(name);
    }
    
    public String getName() {
        return ref.getName();
    }
    
    
    
}
