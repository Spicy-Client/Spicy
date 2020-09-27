package spicy.module.modules.player;


import spicy.module.Category;
import spicy.module.Module;

public class NoHurtCam extends Module
{

    public NoHurtCam() {
        super("NoHurtCam", 0, Category.PLAYER);
    }

    @Override
    public String getModuleDesc() {
        return "No hurt camera";
    }

}
