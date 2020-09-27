package spicy.gui.click.component;


import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.utils.Render;

import java.awt.*;
import java.util.ArrayList;

public class Frame
{
    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private int width;
    private int y;
    private int x;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;

    public Frame(final Category cat) {
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        int tY = this.barHeight + 3;
        for (final Module mod : Spicy.INSTANCE.moduleManager.getModulesInCategory(this.category)) {
            final Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 16;
        }
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public void setX(final int newX) {
        this.x = newX;
    }

    public void setY(final int newY) {
        this.y = newY;
    }

    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public void renderFrame(final FontRenderer fontRenderer) {
        int big = 10;
        for (final Component c : this.components) {
            if (c instanceof Button) {
                final Button btn = (Button)c;
                if (fontRenderer.getStringWidth(btn.mod.getModuleName()) > big) {
                    big = fontRenderer.getStringWidth(btn.mod.getModuleName());
                }
            }
        }
        big += 15;
        this.width = big;
        int heightXD = 0;
        if (this.open && !this.components.isEmpty()) {
            for (final Component component : this.components) {
                heightXD += component.getHeight();
                heightXD += 2;
            }
            Render.drawRect(this.x - 2, this.y + this.barHeight - 0.5f, this.x + this.width + 2, this.y + heightXD + this.barHeight + 4, -1946157056);
            for (final Component component : this.components) {
                component.renderComponent();
            }
        }
        Render.drawRect(this.x - 2, this.y - 2.5, this.x + this.width + 2, this.y + this.barHeight - 0.5f, Color.lightGray.hashCode());
        GlStateManager.color(1,1,1);
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(this.category.name(), (float)(this.x + 2), (float)(int)(this.y + 1.5f), -1);
    }

    public void refresh() {
        int off = this.barHeight + 3;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
            off += 2;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
