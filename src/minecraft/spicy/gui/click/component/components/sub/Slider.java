package spicy.gui.click.component.components.sub;

import net.minecraft.client.gui.Gui;
import spicy.gui.click.component.Component;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;
import spicy.settings.Setting;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component
{
    private boolean hovered;
    private Setting val;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double renderWidth;
    
    public Slider(final Setting value, final Button button, final int offset) {
        this.dragging = false;
        this.val = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        final int drag = (int)(this.val.getValDouble() / this.val.getMax() * this.parent.parent.getWidth());
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() - 2, this.parent.parent.getY() + this.offset + 14, -13290187);
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 6 + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 14, Color.lightGray.hashCode());
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(String.valueOf(this.val.getName()) + ": " + this.val.getValDouble(), (float)(this.parent.parent.getX() + 5), (float)(this.parent.parent.getY() + this.offset + 3), -1);
    }
    
    @Override
    public int getHeight() {
        return 17;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double diff = Math.min(this.parent.parent.getWidth() - 8, Math.max(0, mouseX - this.x));
        final double min = this.val.getMin();
        final double max = this.val.getMax();
        this.renderWidth = (this.parent.parent.getWidth() - 8) * (this.val.getValDouble() - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0) {
                this.val.setValDouble(this.val.getMin());
            }
            else {
                final double newValue = roundToPlace(diff / (this.parent.parent.getWidth() - 8) * (max - min) + min, 2);
                this.val.setValDouble(newValue);
            }
        }
    }
    
    private static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    public boolean isMouseOnButtonD(final int x, final int y) {
        return x > this.x && x < this.x + (this.parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 14;
    }
    
    public boolean isMouseOnButtonI(final int x, final int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 14;
    }
}
