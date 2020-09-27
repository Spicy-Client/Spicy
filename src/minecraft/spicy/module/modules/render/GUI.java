package spicy.module.modules.render;

import com.darkmagician6.eventapi.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import spicy.events.render.Render3DEvent;
import spicy.gui.click.ClickGui;
import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class GUI extends Module {

    public GUI() {
        super("GUI", Keyboard.KEY_RSHIFT, Category.RENDER, false);
    }

    @Override
    public void onToggled() {
        mc.displayGuiScreen(new ClickGui());
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        setModuleState(false);
    }

    @Override
    public void onEnabled() {
        System.out.println("GUI enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("GUI disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Opens the clickable gui";
    }
}
