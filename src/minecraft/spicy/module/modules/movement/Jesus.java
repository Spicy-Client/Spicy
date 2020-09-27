package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import spicy.events.entity.EntityBoundingBoxEvent;
import spicy.events.game.SendPacketEvent;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

public class Jesus extends Module
{
    private boolean next;
    private boolean onWater;
    
    public Jesus() {
        super("Jesus", 0, Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onPre(final PlayerMotionUpdateEvent event) {
        if (event.getState() == PlayerMotionUpdateEvent.State.PRE && Wrapper.isInLiquid() && !Jesus.mc.thePlayer.isSneaking() && Jesus.mc.thePlayer.fallDistance <= 3.0f) {
            Jesus.mc.thePlayer.motionY = 0.085;
        }
    }
    
    @SubscribeEvent
    public void onPre(final SendPacketEvent event) {
        if (event.getState() == SendPacketEvent.PacketState.PRE && event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer player = (C03PacketPlayer)event.getPacket();
            final boolean next = !this.next;
            this.next = next;
            if (next && Wrapper.isOnLiquid()) {
                final C03PacketPlayer c03PacketPlayer = player;
                c03PacketPlayer.y -= 1.0E-6;
            }
        }
    }
    
    @SubscribeEvent
    public void ebb(final EntityBoundingBoxEvent event) {
        if (!this.isModuleState()) {
            return;
        }
        if (Jesus.mc.thePlayer.fallDistance <= 3.0f && !Jesus.mc.thePlayer.isInWater() && !Wrapper.isInLiquid() && event.getBlock() instanceof BlockLiquid && !Jesus.mc.thePlayer.isSneaking()) {
            event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), event.getBlockPos().getX() + 1, event.getBlockPos().getY() + 1, event.getBlockPos().getZ() + 1));
        }
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Can walk on water";
    }
}
