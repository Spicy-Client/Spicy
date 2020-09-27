package spicy.gui.click;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import spicy.gui.click.component.Component;
import spicy.gui.click.component.Frame;
import spicy.main.Spicy;
import spicy.module.Category;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static int color;


    static {
        ClickGui.color = -10110371;
    }

    public ClickGui() {
        ClickGui.frames = new ArrayList<Frame>();
        int frameX = 5;
        int frameY = 5;
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category category = values[i];
            if (frameX > 400) {
                frameX = 5;
                frameY += 14;
            }
            final Frame frame = new Frame(category);
            frame.setX(frameX);
            frame.setY(frameY);
            ClickGui.frames.add(frame);
            frameX += frame.getWidth() + 1;
        }
    }

    @Override
    public void initGui() {
        if(mc.debugFPS > 60 && OpenGlHelper.isFramebufferEnabled()) {
            System.out.println("Your fps better for blur");
            mc.entityRenderer.enableShader(18);
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Frame frame : ClickGui.frames) {
            frame.renderFrame(this.fontRendererObj);
            frame.updatePosition(mouseX, mouseY);
            for (final Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        mc.entityRenderer.disableShader();
        Spicy.INSTANCE.moduleManager.save();
        Spicy.INSTANCE.settingsManager.save();
        super.onGuiClosed();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Frame frame : ClickGui.frames) {
            frame.setDrag(false);
        }
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
