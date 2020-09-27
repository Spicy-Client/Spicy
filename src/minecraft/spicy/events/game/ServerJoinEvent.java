package spicy.events.game;

import com.darkmagician6.eventapi.events.*;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class ServerJoinEvent implements Event
{
    public String ip;

    public ServerJoinEvent(String serverIP) {
        this.ip = serverIP;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
