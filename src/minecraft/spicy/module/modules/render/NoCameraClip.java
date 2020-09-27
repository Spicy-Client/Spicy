package spicy.module.modules.render;

import spicy.module.Category;
import spicy.module.Module;

public class NoCameraClip extends Module {

    public NoCameraClip() {
        super("NoCameraClip", 0, Category.RENDER, true);
    }

    @Override
    public String getModuleDesc() {
        return "Anti camera clip";
    }
}
