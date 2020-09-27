package spicy.events.player;

import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;

public class PlayerSprintingEvent implements Event, Cancellable {
    public boolean cancelled;
    public boolean sprinting;

    public PlayerSprintingEvent(boolean sprinting) {
        this.setSprinting(sprinting);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
