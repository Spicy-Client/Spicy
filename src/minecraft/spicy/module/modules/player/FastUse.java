package spicy.module.modules.player;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class FastUse extends Module {

    public FastUse() {
        super("FastUse", 0, Category.PLAYER, true);
    }


    @Override
    public void onEnabled() {
        System.out.println("FastUse enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("FastUse disabled.");
        super.onDisabled();
    }

    @SubscribeEvent
    public void onUpdate(PlayerMotionUpdateEvent event) {
        if (event.getState() == PlayerMotionUpdateEvent.State.PRE && Wrapper.player().getItemInUseDuration() == 16 && !(Wrapper.player().getItemInUse().getItem() instanceof ItemBow)) {
            for (int i = 0; i < 17; ++i) {
                Wrapper.packet(new C03PacketPlayer(true));
            }
            Wrapper.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
}


    @Override
    public String getModuleDesc() {
        return "Fast using items";
    }
}
