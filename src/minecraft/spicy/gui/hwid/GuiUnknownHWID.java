package spicy.gui.hwid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import spicy.main.Spicy;
import spicy.utils.GLSLSandboxShader;

import java.io.IOException;

public class GuiUnknownHWID extends GuiScreen {
    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();

    public GuiUnknownHWID() {
        try {
            this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }
    }

    @Override
    public void initGui() {
        initTime = System.currentTimeMillis();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
        GlStateManager.disableCull();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);
        Spicy.INSTANCE.fontManager.getFont("FONT 50").drawCenteredString("Spicy", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 3, -1);
        Spicy.INSTANCE.fontManager.getFont("FONT 20").drawCenteredString("why do i see this page?!", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 3 + 30, -1);
        Spicy.INSTANCE.fontManager.getFont("FONT 20").drawCenteredString("Sorry, but your hardware ID was not found in the database.", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 3 + 40, -1);


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }
}
