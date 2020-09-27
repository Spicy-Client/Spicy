package spicy.events.game;

import com.darkmagician6.eventapi.events.*;
import net.minecraft.network.*;

public class SendPacketEvent implements Event, Cancellable
{
    private boolean cancelled;
    private Packet packet;
    private PacketState state;

    public SendPacketEvent(final Packet packet, final PacketState state) {
        this.packet = packet;
        this.state = state;
    }

    public PacketState getState() {
        return this.state;
    }

    public void setPacket(final Packet packet) {
        this.packet = packet;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public enum PacketState
    {
        PRE,
        POST;
    }
}
