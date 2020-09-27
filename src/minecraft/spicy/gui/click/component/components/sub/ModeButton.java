package spicy.gui.click.component.components.sub;


import spicy.gui.click.component.Component;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;
import spicy.module.Module;
import spicy.settings.Setting;

public class ModeButton extends Component
{
    private boolean hovered;
    private Button parent;
    private Setting setting;
    private int offset;
    private int x;
    private int y;
    private Module mod;
    
    public ModeButton(final Setting setting, final Button button, final Module mod, final int offset) {
        this.setting = setting;
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
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow("Mode: " + this.setting.getValString(), (float)(this.parent.parent.getX() + 6), (float)(this.parent.parent.getY() + this.offset + 3), -1);
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
            int i = 0;
            int count = 0;
            for (final String s : this.setting.getOptions()) {
                if (s == this.setting.getValString()) {
                    i = count;
                }
                ++count;
            }
            if (i + 1 >= count) {
                i = 0;
            }
            this.setting.setValString(this.setting.getOptions().get(i + 1));
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 14;
    }
}
