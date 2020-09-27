package spicy.events.player;

import com.darkmagician6.eventapi.events.*;

public class PlayerMotionUpdateEvent implements Event, Cancellable
{
    private boolean cancelled;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean overload;
    private State state;

    public PlayerMotionUpdateEvent(final float yaw, final float pitch, final double x, final double y, final double z, final boolean onGround, final State state) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        this.state = state;
    }

    public PlayerMotionUpdateEvent() {
        this.state = State.POST;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public State getState() {
        return this.state;
    }

    public boolean getOverload() {
        return this.overload;
    }

    public void setOverload(final boolean overload) {
        this.overload = overload;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public enum State
    {
        PRE,
        POST;
    }
}
