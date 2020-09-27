package spicy.gui.click.component.components.sub;


import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import spicy.gui.click.component.Component;
import spicy.gui.click.component.components.Button;
import spicy.main.Spicy;

public class Keybind extends Component
{
    private boolean hovered;
    private boolean binding;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    
    public Keybind(final Button button, final int offset) {
        this.parent = button;
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
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(this.binding ? "Press a key..." : ("Key: " + Keyboard.getKeyName(this.parent.mod.getModuleKey())), (float)(this.parent.parent.getX() + 6), (float)(this.parent.parent.getY() + this.offset + 3), -1);
        GlStateManager.color(1,1,1);
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
            this.binding = !this.binding;
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        if (this.binding) {
            if (key == 211 || key == 57) {
                this.parent.mod.setModuleKey(0);
                this.binding = false;
                return;
            }
            this.parent.mod.setModuleKey(key);
            this.binding = false;
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y - 1 && y < this.y + 14;
    }
    
    @Override
    public int getHeight() {
        return 16;
    }
}
