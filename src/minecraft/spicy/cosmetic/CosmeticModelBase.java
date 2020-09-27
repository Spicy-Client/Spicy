package spicy.cosmetic;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class CosmeticModelBase extends ModelBase {
    protected final ModelBiped modelBiped;

    public CosmeticModelBase(RenderPlayer player) {
        this.modelBiped = player.getMainModel();
    }
}
