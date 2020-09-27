package spicy.module.modules.render;

import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class EnchantColor extends Module {

    public EnchantColor() {
        super("EnchantColor", 0, Category.RENDER, true);
    }

    @Override
    public void onEnabled() {
        System.out.println("Fullbright enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("Fullbright disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Changes enchanted items glint color";
    }
}
