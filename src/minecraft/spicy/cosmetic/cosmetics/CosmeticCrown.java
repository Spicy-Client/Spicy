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

public class CosmeticCrown extends Cosmetic {
    private final ModelCrown modelCrown;
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("spicy/cosmetics/crown.png");

    public CosmeticCrown(RenderPlayer renderPlayer) {
        super(renderPlayer);
        modelCrown = new ModelCrown(renderPlayer);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        if(CosmeticHandler.shouldRenderCrown(player)) {
            GlStateManager.pushMatrix();
            playerRenderer.bindTexture(RESOURCE_LOCATION);
            float[] color = CosmeticHandler.getCrownColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            modelCrown.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1,1,1);
            GL11.glPopMatrix();
        }
    }

    private class ModelCrown extends CosmeticModelBase {
        private ModelRenderer base;
        private ModelRenderer diamond;
        private Color diamondColor;


        public ModelCrown(RenderPlayer player) {
            super(player);
            float f = 0.02F;
            this.base = (new ModelRenderer(modelBiped, 0, 0)).setTextureSize(22, 7);
            this.base.setTextureOffset(4, 0).addBox(-4.0F, 0.0F, -5.0F, 8, 2, 1, f);
            this.base.setTextureOffset(0, 0).addBox(-5.0F, -2.0F, -5.0F, 1, 4, 1, f);
            this.base.setTextureOffset(0, 5).addBox(-4.0F, -1.0F, -5.0F, 1, 1, 1, f);
            this.base.setTextureOffset(0, 5).addBox(3.0F, -1.0F, -5.0F, 1, 1, 1, f);
            this.base.setTextureOffset(4, 5).addBox(-1.5F, -1.0F, -5.0F, 3, 1, 1, f);
            this.base.setTextureOffset(0, 5).addBox(-0.5F, -2.0F, -5.0F, 1, 1, 1, f);
            this.base.isHidden = true;
            this.diamond = (new ModelRenderer(modelBiped, 12, 5)).setTextureSize(22, 7);
            this.diamond.addBox(-0.5F, -0.0F, -6.0F, 1, 1, 1, f);
            this.diamond.rotateAngleZ = 0.8F;
            this.diamond.rotationPointZ = 0.5F;
            this.diamond.rotationPointX = 0.4F;
            this.diamond.isHidden = true;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            for (int i = 0; i < 4; ++i) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F);
                GlStateManager.rotate(headYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(headPitch, 1.0F, 0.0F, 0.0F);
                float f = 1.085F;
                GlStateManager.scale(f, f, f);

                if (entityIn.isSneaking()) {
                    float f1 = entityIn.rotationPitch * -7.0E-4F;
                    GlStateManager.translate(0.0D, (double) (0.06F - Math.abs(f1)) + 0.2D, (double) f1);
                }

                GlStateManager.rotate((float) (90 * i), 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0D, -0.4753D, 0.0D);
                Minecraft.getMinecraft().getTextureManager().bindTexture(RESOURCE_LOCATION);
                this.base.isHidden = false;
                this.base.render(0.0571F);
                this.base.isHidden = true;
                this.diamond.isHidden = false;
                this.diamond.rotateAngleZ = 0.8F;
                this.diamond.rotationPointZ = 0.6F;
                this.diamond.rotationPointX = 0.4F;
                GlStateManager.translate(-0.22F, 0.0F, 0.0F);
                GlStateManager.color(1.0F, 1.0F, 1.0F);
                Color colord = getDiamondColor();

                if (colord != null) {
                    GL11.glColor4f((float) colord.getRed() / 1, (float) colord.getGreen() / 1, (float) colord.getBlue() / 255.0F, 0.5F);
                }

                for (int j = 0; j < 3; ++j) {
                    this.diamond.render(0.0561F);
                    GlStateManager.translate(0.218F, 0.0F, 0.0F);
                }

                float[] color = CosmeticHandler.getCrownColor(Minecraft.getMinecraft().thePlayer);
                GL11.glColor3d(color[0], color[1], color[2]);
                GlStateManager.color(1.0F, 1.0F, 1.0F);
                this.diamond.isHidden = true;
                GlStateManager.popMatrix();
            }
        }
        public Color getDiamondColor()
        {
            return this.diamondColor;
        }
    }
}
