package spicy.events.render;

import com.darkmagician6.eventapi.events.*;

public class Render3DEvent implements Event, Cancellable
{
    private boolean cancelled;
    private float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
