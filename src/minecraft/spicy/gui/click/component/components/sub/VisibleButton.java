package spicy.gui.click.component.components.sub;

import spicy.gui.click.component.Component;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;
import spicy.module.Module;

public class VisibleButton extends Component
{
    private boolean hovered;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    private Module mod;
    
    public VisibleButton(final Button button, final Module mod, final int offset) {
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {
        if(mod.isModuleVisible()) {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow("Visible : true", (float)(this.parent.parent.getX() + 5), (float)(this.parent.parent.getY() + this.offset + 3), -1);

        } else {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow("Visible : false", (float)(this.parent.parent.getX() + 5), (float)(this.parent.parent.getY() + this.offset + 3), -1);
        }
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
            this.mod.setModuleVisible(!this.mod.isModuleVisible());
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 10;
    }
}
