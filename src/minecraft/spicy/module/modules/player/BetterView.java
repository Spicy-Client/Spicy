package spicy.module.modules.player;

import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.user.User;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class BetterView extends Module {

    public BetterView() {
        super("BetterView", 0, Category.PLAYER, true);
    }


    @Override
    public void onEnabled() {
        if(Spicy.INSTANCE.user.getRank() == User.RankGroup.User) {
            ModuleManager.getModule(BetterView.class).toggle();
        }
        System.out.println("BetterView enabled.");
        super.onEnabled();
    }


    @Override
    public void onDisabled() {
        System.out.println("BetterView disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Better person view";
    }
}
