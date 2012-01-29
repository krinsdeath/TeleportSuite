package net.krinsoft.teleportsuite;

/**
 * @author krinsdeath
 */
public class Request {
    public enum Type {
        TPA,
        TPAHERE,
        TP,
        TPHERE,
        TPO,
        TPOHERE
    }
    
    TeleportPlayer dest;
    Type type;
    
    public Request(TeleportPlayer player, Type request) {
        this.dest = player;
        this.type = request;
    }
    
    TeleportPlayer getTo() { return this.dest; }
    
    Type getType() { return this.type; }
    
    @Override
    public boolean equals(Object that) {
        return this == that || this.getClass() == that.getClass() && this.dest.getName().equals(((Request) that).dest.getName());
    }
    
}
