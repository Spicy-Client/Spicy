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

public class CosmeticWings extends Cosmetic {
    private final ModelAngelWhiteWings modelAngelWhiteWings;
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("spicy/cosmetics/white.png");


    public CosmeticWings(RenderPlayer renderPlayer) {
        super(renderPlayer);
        modelAngelWhiteWings = new ModelAngelWhiteWings(renderPlayer);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        if(CosmeticHandler.shouldRenderWings(player)) {
            GlStateManager.pushMatrix();
            playerRenderer.bindTexture(RESOURCE_LOCATION);
            float[] color = CosmeticHandler.getAngelWhiteWingsColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            modelAngelWhiteWings.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1, 1, 1);
            GL11.glPopMatrix();
        }
    }

    private class ModelAngelWhiteWings extends CosmeticModelBase {
        ModelRenderer rightWing;
        ModelRenderer leftWing;

        public ModelAngelWhiteWings(RenderPlayer player) {
            super(player);
            this.leftWing = new ModelRenderer(modelBiped, 0, 0);
            this.leftWing.setTextureSize(64, 32);
            this.leftWing.addBox(-8.5F, 0.0F, -0.5F, 17, 30, 1);
            this.rightWing = new ModelRenderer(modelBiped, 0, 0);
            this.rightWing.setTextureSize(64, 32);
            this.rightWing.addBox(-8.5F, 0.0F, -0.5F, 17, 30, 1);
            this.rightWing.mirror = true;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
                this.leftWing.offsetY = 0.2f;
                this.leftWing.offsetX = -0.2f;
                this.rightWing.offsetX = -0.2f;
                this.rightWing.offsetZ = -0.1f;
                this.leftWing.offsetZ = -0.1f;
                this.rightWing.offsetY = 0.2f;
                float angle = getWingAngle(Minecraft.getMinecraft().thePlayer.capabilities.isFlying & Minecraft.getMinecraft().thePlayer.isAirBorne, 30.0F, 5000, 400, Minecraft.getMinecraft().thePlayer.getEntityId());

                setRotation(this.leftWing, (float) Math.toRadians((double) (angle + 20.0F)), (float) Math.toRadians(-4.0D), (float) Math.toRadians(6.0D));
                setRotation(this.rightWing, (float) Math.toRadians((double) (-angle - 20.0F)), (float) Math.toRadians(4.0D), (float) Math.toRadians(6.0D));

                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 4.0F * 0.0625F, 1.5F * 0.0625F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glColor3f(1, 1, 1);
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 0.0F, 0.75f * 3.0F * 0.0625F);
                GL11.glScalef(0.75F, 0.75F, 0.75F);
                this.leftWing.offsetZ = 0.0010f;
                this.leftWing.render(0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 0.0F, -0.75f * 3.0F * 0.0625F);
                GL11.glScalef(0.75F, 0.75F, 0.75F);
                this.rightWing.offsetZ = 0.0010f;
                this.rightWing.render(0.0625F);
                GL11.glPopMatrix();
                GL11.glPopMatrix();
        }
    }
}
