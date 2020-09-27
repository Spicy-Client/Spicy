package spicy.module.modules.render;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import spicy.events.game.GameTickEvent;
import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", 0, Category.RENDER, true);
    }

    @Override
    public void onEnabled() {
        System.out.println("Fullbright enabled.");

        super.onEnabled();
    }

    @SubscribeEvent
    public void onTick(GameTickEvent event) {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }

    @Override
    public void onDisabled() {
        System.out.println("Fullbright disabled.");
        mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Infinity night vision";
    }
}
