package spicy.events.render;

import com.darkmagician6.eventapi.events.*;

public class Render2DEvent implements Event, Cancellable
{
    private int width;
    private int height;
    private boolean cancelled;

    public Render2DEvent(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(final int height) {
        this.height = height;
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
