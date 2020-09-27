package spicy.module.modules.render;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.renderer.OpenGlHelper;
import spicy.events.render.Render2DEvent;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.notification.NotificationPublisher;
import spicy.notification.NotificationType;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class MiniMap extends Module {

    public MiniMap() {
        super("MiniMap", 0, Category.RENDER, true);
    }

    @Override
    public void onEnabled() {
        System.out.println("MiniMap enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("MiniMap disabled.");
        super.onDisabled();
    }

    @SubscribeEvent
    public void onRender(Render2DEvent event) {
        if(OpenGlHelper.isFramebufferEnabled()) {
            ModuleManager.getModule(MiniMap.class).setModuleState(false);
            NotificationPublisher.queue(getModuleName(), getFramebuffer(), NotificationType.WARNING);
        }
    }


    public String getFramebuffer() {
        return "please enable fast render";
    }

    @Override
    public String getModuleDesc() {
        return "Shows the minimap";
    }

}
