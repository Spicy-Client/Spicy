package spicy.module.modules.render;

import spicy.module.Category;
import spicy.module.Module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class NoSkin extends Module {

    public NoSkin() {
        super("NoSkin", 0, Category.RENDER, true);
    }


    @Override
    public void onEnabled() {
        System.out.println("NoSkin enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("NoSkin disabled.");
        super.onDisabled();
    }



    @Override
    public String getModuleDesc() {
        return "No Skin for players";
    }
}
