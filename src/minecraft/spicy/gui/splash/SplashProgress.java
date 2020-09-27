package spicy.gui.splash;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import spicy.font.UnicodeFontRenderer;

import java.awt.*;

public class SplashProgress {

    private static final int MAX = 13;
    private static int PROGRESS = 0;
    private static String CURRENT = "";
    private static ResourceLocation splash;
    private static UnicodeFontRenderer ufr;
    private static UnicodeFontRenderer ufr2;
    private static UnicodeFontRenderer ufr3;



    public static void update() {
        if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    public static void setProgress(int givenProgress, String givenText) {
        PROGRESS = givenProgress;
        CURRENT = givenText;
        update();
    }

    public static void drawSplash(TextureManager tm) {

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = scaledResolution.getScaleFactor();

        Framebuffer framebuffer = new Framebuffer(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(true);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D,  (double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        if(splash == null) {
            splash = new ResourceLocation("spicy/background.png");
        }
        tm.bindTexture(splash);

        GlStateManager.resetColor();
        GlStateManager.color(1.0F,  1.0F, 1.0F, 1.0F);

        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 1920, 1080);
        drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor);

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        Minecraft.getMinecraft().updateDisplay();

    }

    private static void drawProgress() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if(Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) {
            return;
        }
        if(ufr3 == null) {
            ufr3 = UnicodeFontRenderer.getFontOnPC("Verdana", 15);
        }
        String step = PROGRESS + "/" + MAX;
        ufr3.drawCenteredString(step, 123123, 123123,-1);
        ufr3.drawCenteredString(CURRENT, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 10, Color.white.hashCode());
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        double nProgress = (double)PROGRESS;
        double calc = (nProgress / MAX) * sr.getScaledWidth();
    }

    private static void resetTextureState() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }

}
