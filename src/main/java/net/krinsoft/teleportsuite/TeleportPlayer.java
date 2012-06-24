package net.krinsoft.teleportsuite;

import net.krinsoft.teleportsuite.destinations.TPDestination;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author krinsdeath
 */
public class TeleportPlayer {
    private final static Pattern colors = Pattern.compile("&([0-9a-fA-F])");

    public enum Status {
        ACCEPTING,
        REJECTING,
        AUTO;
        
        public static Status fromName(String name) {
            if (name == null) { return ACCEPTING; }
            for (Status value : values()) {
                if (value.getName().equals(name.toLowerCase())) {
                    return value;
                }
            }
            return ACCEPTING;
        }
        
        public String getName() {
            return name().toLowerCase();
        }
    }

    private TeleportSuite plugin;
    private String name;
    private List<TPDestination> stack = new ArrayList<TPDestination>();
    private List<Request> requests = new ArrayList<Request>();
    private String language;
    private String request;
    private boolean requesting;
    private boolean silent;
    private Status status;
    
    public TeleportPlayer(TeleportSuite plugin, String name) {
        this.plugin = plugin;
        this.name = plugin.getServer().getPlayer(name).getName();
        this.silent = plugin.getUsers().getBoolean(name + ".silent");
        this.language = plugin.getUsers().getString(name + ".language");
        if (this.language == null) {
            this.language = plugin.getConfig().getString("languages.default");
        }
        String tmp = plugin.getUsers().getString(name + ".status");
        if (tmp == null) {
            tmp = plugin.getLocalization(language).get("status.toggle.default", name);
        }
        this.status = Status.fromName(tmp);
        this.stack.clear();
        pushToStack(plugin.getServer().getPlayer(name).getLocation());
    }

    public void clean() {
        stack = null;
        for (Request r : new ArrayList<Request>(requests)) {
            plugin.getManager().finish(r.getTo(), r, false);
        }
    }
    
    public Player getReference() {
        return plugin.getServer().getPlayer(name);
    }
    
    public boolean hasPermission(String node) {
        boolean t = plugin.getServer().getPlayer(name).hasPermission(node);
        if (!t) {
            sendLocalizedString("error.permission", node);
        }
        return t;
    }

    public String getName() {
        return name;
    }

    public void setLanguage(String lang) {
        language = lang;
        sendLocalizedString("default.change", null);
    }

    public void save() {
        plugin.getUsers().set(name + ".silent"       , silent);
        plugin.getUsers().set(name + ".language"     , language);
        plugin.getUsers().set(name + ".status"       , status.getName());
    }

    /**
     * Pushes a location into this player's teleport stack
     * @param loc The location to add to the front of the stack
     * @return The TPDestination representing the location we just pushed into the stack
     */
    public TPDestination pushToStack(Location loc) {
        TPDestination dest = new TPDestination(plugin, loc);
        if (stack.size() > 0 && stack.get(0) != null && stack.get(0).equals(dest)) {
            return dest;
        }
        stack.add(0, dest);
        if (stack.size() > 5) {
            stack = stack.subList(0, 4);
        }
        return dest;
    }

    /**
     * Rewinds the player to his last location and returns the destination
     * @return the player's new location 
     */
    public TPDestination rewind() {
        if (stack.isEmpty()) { return null; }
        TPDestination tp = stack.remove(0);
        if (stack.isEmpty()) { return tp; }
        tp = stack.remove(0);
        plugin.getServer().getPlayer(name).teleport(tp.getLocation());
        sendLocalizedString("teleport.tpback", getName());
        return tp;
    }

    /**
     * Rewinds the player the specified number of teleports
     * @param places The number of locations to rewind the player, a maximum of 5
     * @return A list of locations that were rewinded
     */
    public List<TPDestination> rewind(int places) {
        if (places > 5) { places = 5; }
        if (stack.isEmpty()) { return stack; }
        if (places > stack.size()) { places = stack.size(); }
        List<TPDestination> list = new ArrayList<TPDestination>();
        for (int i = 0; i < places; i++) {
            if (stack.isEmpty()) { break; }
            list.add(stack.remove(0));
        }
        sendLocalizedString("teleport.tprewind", String.valueOf(places));
        plugin.getServer().getPlayer(name).teleport(list.get(list.size() - 1).getLocation());
        return list;
    }

    /**
     * Teleports the player representing this object to the location specified
     * @param location The location to send the player
     * @return A TPDestination object representing the player's previous location
     */
    public TPDestination teleport(Location location) {
        TPDestination last = pushToStack(plugin.getServer().getPlayer(name).getLocation());
        plugin.getServer().getPlayer(name).teleport(location);
        return last;
    }
    
    public void sendMessage(String... message) {
        Player p = plugin.getServer().getPlayer(name);
        if (message == null || p == null) { return; }
        for (String line : message) {
            line = colors.matcher(line).replaceAll("\u00A7$1");
            p.sendMessage(line);
        }
    }

    /**
     * Sends a localized string based on the key provided
     * @param key The value of the key in the localization file to send
     * @param token Replace variables in the returned string with this value
     */
    public void sendLocalizedString(String key, String token) {
        String msg = plugin.getLocalization(language).get(key, token);
        if (msg != null && msg.length() > 0) {
            sendMessage(msg.split("\n"));
        }
    }

    public boolean isSilent() {
        return silent;
    }

    public void toggleSilence() {
        silent = !silent;
        if (!silent) {
            sendLocalizedString("status.silent.disabled", getName());
        } else {
            sendLocalizedString("status.silent.enabled", getName());
        }
    }

    public boolean isRequesting() {
        return requesting;
    }

    public void setRequesting(String name, boolean val) {
        request = name;
        requesting = val;
    }
    
    public String getActive() {
        return this.request;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(Status s) {
        sendLocalizedString("status.toggle." + s.getName(), this.getName());
        this.status = s;
    }
    
    public Location getLocation() {
        return plugin.getServer().getPlayer(name).getLocation();
    }
    
    public List<Request> getRequests() {
        return requests;
    }
    
    public Request getRequest(String name) {
        for (Request r : requests) {
            if (r.getTo().getName().equals(name)) {
                return r;
            }
        }
        return null;
    }
    
    public void cancelRequest() {
        if (request != null) {
            TeleportPlayer p = plugin.getManager().getPlayer(request);
            Request r = p.getRequest(getName());
            if (r != null) {
                p.getRequests().remove(r);
                p.sendLocalizedString("error.requests.canceled", getName());
            }
            sendLocalizedString("teleport.tpcancel", request);
            request = null;
            requesting = false;
        }
    }
    
}
