package spicy.gui.click.component.components.sub;


import spicy.gui.click.component.Component;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;
import spicy.settings.Setting;

import java.awt.*;

public class Checkbox extends Component
{
    private boolean hovered;
    private Setting op;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    
    public Checkbox(final Setting option, final Button button, final int offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(this.op.getName(), (float)(this.parent.parent.getX() + 5), (float)(this.parent.parent.getY() + this.offset + 3), this.op.getValBoolean() ? -1 : Color.lightGray.hashCode());
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.op.setValBoolean(!this.op.getValBoolean());
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 14;
    }
}
