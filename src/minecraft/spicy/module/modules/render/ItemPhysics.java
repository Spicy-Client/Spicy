package spicy.module.modules.render;


import spicy.module.Category;
import spicy.module.Module;

public class ItemPhysics extends Module
{

    public ItemPhysics() {
        super("ItemPhysics", 0, Category.RENDER);
    }

    @Override
    public String getModuleDesc() {
        return "Items physic";
    }

}
