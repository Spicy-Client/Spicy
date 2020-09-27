package spicy.cosmetic.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import spicy.cosmetic.Cosmetic;
import spicy.cosmetic.CosmeticHandler;
import spicy.cosmetic.CosmeticModelBase;

import java.awt.*;

public class CosmeticHalo extends Cosmetic {
    private final ModelHalo modelHalo;
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("spicy/cosmetics/halo.png");

    public CosmeticHalo(RenderPlayer renderPlayer) {
        super(renderPlayer);
        modelHalo = new ModelHalo(renderPlayer);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        if(CosmeticHandler.shouldRenderHalo(player)) {
            GlStateManager.pushMatrix();
            playerRenderer.bindTexture(RESOURCE_LOCATION);
            float[] color = CosmeticHandler.getHaloColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            modelHalo.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1,1,1);
            GL11.glPopMatrix();
        }
    }

    private class ModelHalo extends CosmeticModelBase {
        private ModelRenderer halo;
        private boolean hat = true;

        public ModelHalo(RenderPlayer player) {
            super(player);
            this.halo = (new ModelRenderer(modelBiped)).setTextureSize(14, 2);
            this.halo.addBox(-3.0F, -12.5F, -4.0F, 6, 1, 1, 0.15f);
            this.halo.isHidden = true;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            GlStateManager.pushMatrix();
            float f = (float)Math.cos((double)ageInTicks / 10.0D) / 20.0F;
            GlStateManager.rotate(headYaw + ageInTicks / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, f - (isHat() ? 0.2F : 0.0F), 0.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(RESOURCE_LOCATION);
            GlStateManager.disableLighting();
            ModelRenderer modelrenderer = bindTextureAndColor(Color.GREEN, RESOURCE_LOCATION, this.halo, (ModelRenderer)null);
            modelrenderer.isHidden = false;

            for (int i = 0; i < 4; ++i)
            {
                modelrenderer.render(scale);
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            }

            modelrenderer.isHidden = true;
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

        public boolean isHat()
        {
            return this.hat;
        }

        private Color getColor() {
            return Color.GREEN;
        }

    }
}
