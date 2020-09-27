package spicy.module.modules.movement;


import com.darkmagician6.eventapi.SubscribeEvent;
import spicy.events.player.PlayerMoveEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

public class FastFall extends Module
{

    public FastFall() {
        super("Fast Fall", 0, Category.MOVEMENT);
    }


    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        event.setY(Wrapper.player().motionY = -3.0);
    }

    @Override
    public String getModuleDesc() {
        return "Fast fall while player falling";
    }

}
