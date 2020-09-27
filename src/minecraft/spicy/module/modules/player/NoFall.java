package spicy.module.modules.player;


import com.darkmagician6.eventapi.SubscribeEvent;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.module.Category;
import spicy.module.Module;

public class NoFall extends Module
{

    public NoFall() {
        super("NoFall", 0, Category.PLAYER);
    }
    @SubscribeEvent
    private void onUpdate(final PlayerMotionUpdateEvent event) {
            event.setOnGround(true);
    }

    @Override
    public String getModuleDesc() {
        return "No fall damage";
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
