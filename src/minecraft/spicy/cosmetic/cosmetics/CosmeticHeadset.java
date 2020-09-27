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

public class CosmeticHeadset extends Cosmetic {
    private final ModelHeadset modelHeadset;
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("spicy/cosmetics/headset.png");
    private boolean mic = true;

    public CosmeticHeadset(RenderPlayer renderPlayer) {
        super(renderPlayer);
        modelHeadset = new ModelHeadset(renderPlayer);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        if(CosmeticHandler.shouldRenderHeadset(player)) {
            GlStateManager.pushMatrix();
            playerRenderer.bindTexture(RESOURCE_LOCATION);
            float[] color = CosmeticHandler.getHeadsetColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            modelHeadset.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1,1,1);
            GL11.glPopMatrix();
        }
    }

    private class ModelHeadset extends CosmeticModelBase {
        private ModelRenderer earCup;
        private ModelRenderer headBandSide;
        private ModelRenderer headBandTop;
        private ModelRenderer mic;

        public ModelHeadset(RenderPlayer player) {
            super(player);
            int i = 18;
            int j = 7;
            this.earCup = (new ModelRenderer(this, 0, 0)).setTextureSize(i, j).setTextureOffset(0, 0);
            this.earCup.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 1, 0.25f);
            this.earCup.isHidden = true;
            this.headBandSide = (new ModelRenderer(this, 0, 0)).setTextureSize(i, j).setTextureOffset(8, 0);
            this.headBandSide.addBox(-0.5F, -4.0F, 0.0F, 1, 3, 1, 0.25f);
            this.headBandSide.isHidden = true;
            this.headBandTop = (new ModelRenderer(this, 0, 0)).setTextureSize(i, j).setTextureOffset(0, 5);
            this.headBandTop.addBox(-4.0F, 0.0F, -2.0F, 8, 1, 1, 0.25f);
            this.headBandTop.isHidden = true;
            this.mic = (new ModelRenderer(this, 0, 0)).setTextureSize(i, j).setTextureOffset(12, 0);
            this.mic.addBox(-0.5F, -4.0F, 0.0F, 1, 4, 1, 0.25f);
            this.mic.isHidden = true;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(RESOURCE_LOCATION);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.2F, 1.2F, 1.2F);

            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0D, 0.2D, 0.0D);
            }

            GlStateManager.rotate(headYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(headPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0D, -0.3D, 0.1D);
            this.earCup.isHidden = false;
            this.headBandSide.isHidden = false;
            this.headBandTop.isHidden = false;
            this.mic.isHidden = false;
            double d0 = 0.21D;
            double d1 = 0.1D;
            double d2 = 0.6D;
            double d3 = -0.0317D;

            for (int i = -1; i < 2; i += 2)
            {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

                if (i == 1)
                {
                    GlStateManager.scale(1.0F, 1.0F, -1.0F);
                }

                GlStateManager.translate(0.1D, 0.1D, d0);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0D, d3, 0.0D);
                this.headBandSide.render(scale);

                if (isMic() && i == -1)
                {
                    GlStateManager.translate(0.028D, -d3 + 0.05D, 0.0D);
                    GlStateManager.scale(0.8D, 0.8D, 0.8D);
                    GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
                    this.headBandSide.render(scale);
                    GlStateManager.scale(0.65D, 0.65D, 0.65D);
                    GlStateManager.translate(0.01D, -0.37D, 0.08D);
                    GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(-60.0F, -1.0F, 0.0F, 0.0F);
                    this.mic.render(scale);
                }

                GlStateManager.popMatrix();
                this.earCup.render(scale);
                GlStateManager.scale(d2, d2, d2);
                GlStateManager.translate(0.0D, 0.0D, d1);
                GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
                this.earCup.render(scale);
                GlStateManager.popMatrix();
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0D, -0.1817D, -0.0063D);
            GlStateManager.scale(0.83999D, 1.0D, 1.0D);
            this.headBandTop.render(scale);
            GlStateManager.popMatrix();
            this.earCup.isHidden = true;
            this.headBandSide.isHidden = true;
            this.headBandTop.isHidden = true;
            this.mic.isHidden = true;
            GlStateManager.popMatrix();
        }
    }

    public boolean isMic()
    {
        return this.mic;
    }
}
