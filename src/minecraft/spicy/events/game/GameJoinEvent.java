package spicy.events.game;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.NetworkManager;

public class GameJoinEvent implements Event {
    private NetworkManager networkManager;

    public GameJoinEvent(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

}
