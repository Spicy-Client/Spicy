package spicy.module.modules.movement;


import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

public class NoSlowdown extends Module
{
    public NoSlowdown() {
        super("NoSlowdown", 0, Category.MOVEMENT, true);
    }
    

    
    @SubscribeEvent(4)
    public void onMotion(final PlayerMotionUpdateEvent event) {
        if (NoSlowdown.mc.thePlayer.isBlocking() && NoSlowdown.mc.thePlayer.motionX != 0.0 && NoSlowdown.mc.thePlayer.motionZ != 0.0) {
            if (event.getState() == PlayerMotionUpdateEvent.State.PRE) {
                Wrapper.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            else if (event.getState() == PlayerMotionUpdateEvent.State.POST) {
                Wrapper.packet(new C08PacketPlayerBlockPlacement(NoSlowdown.mc.thePlayer.inventory.getCurrentItem()));
            }
        }
    }

    @Override
    public void onEnabled() {
        System.out.println("Noslowdown enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("Noslowdown disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "No slows when using item";
    }
}
