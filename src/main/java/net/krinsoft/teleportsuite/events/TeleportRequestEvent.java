package net.krinsoft.teleportsuite.events;

import net.krinsoft.teleportsuite.Request;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author krinsdeath
 */
public class TeleportRequestEvent extends Event {
    private final static HandlerList handlerlist = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerlist;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    private final String from;
    private final String to;
    private final Request.Type type;

    public TeleportRequestEvent(String playerFrom, String playerTo, Request.Type type) {
        this.from = playerFrom;
        this.to = playerTo;
        this.type = type;
    }

    /**
     * Fetch the name of the player who requested the teleport.
     * @return The name of the player
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * Fetches the name of the player who received the teleport request.
     * @return The name of the player
     */
    public String getTo() {
        return this.to;
    }

    /**
     * Fetches the type of the request.
     * @return The request type
     */
    public Request.Type getType() {
        return this.type;
    }
}
