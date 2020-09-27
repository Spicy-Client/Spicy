package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import spicy.events.render.Render2DEvent;
import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class ContainerWalk extends Module {

    public ContainerWalk() {
        super("ContainerWalk", 0, Category.MOVEMENT, true);
    }



    @SubscribeEvent
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown(200)) {
                mc.thePlayer.rotationPitch -= 2.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                mc.thePlayer.rotationPitch += 2.0f;
            }
            if (Keyboard.isKeyDown(203)) {
                mc.thePlayer.rotationYaw -= 3.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                mc.thePlayer.rotationYaw += 3.0f;
            }
        }
    }


    @Override
    public void onEnabled() {
        System.out.println("ContainerWalk enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("ContainerWalk disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Can move on container";
    }
}
