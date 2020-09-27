package spicy.gui.click.component.components;

import spicy.gui.click.component.Component;
import spicy.gui.click.component.Frame;
import spicy.gui.click.component.components.sub.Checkbox;
import spicy.gui.click.component.components.sub.*;
import spicy.main.Spicy;
import spicy.module.Module;
import spicy.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component
{
    public Module mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    private ArrayList<Component> subcomponents;
    public boolean open;
    private int height;
    
    public Button(final Module mod, final Frame parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 12;
        int opY = offset + 12;
        if (Spicy.INSTANCE.settingsManager.getSettingsByMod(mod) != null) {
            for (final Setting s : Spicy.INSTANCE.settingsManager.getSettingsByMod(mod)) {
                if(s.isCombo()) {
                    this.subcomponents.add(new ModeButton(s,this, mod, opY));
                    opY += 12;
                }
                if (s.isSlider()) {
                    final Setting num = (Setting) s;
                    final Slider slider = new Slider(num, this, opY);
                    this.subcomponents.add(slider);
                    opY += 12;
                }
                if (s.isCheck()) {
                    final Setting bool = (Setting) s;
                    final Checkbox check = new Checkbox(bool, this, opY);
                    this.subcomponents.add(check);
                    opY += 12;
                }
            }
        }
        this.subcomponents.add(new VisibleButton(this, mod, opY));
        this.subcomponents.add(new Keybind(this, opY));

    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 15;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 14;
        }
    }
    
    @Override
    public void renderComponent() {
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(this.mod.getModuleName(), (float)(this.parent.getX() + 4), (float)(this.parent.getY() + this.offset + 2 + 1), mod.isModuleState() ? -1 : Color.lightGray.hashCode());
       if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (!this.open) {
            return 14;
        }
        if (!Spicy.INSTANCE.settingsManager.getSettings().isEmpty()) {
            return 14 * (this.subcomponents.size() - 1) + 13 + 13 + 3;
        }
        return 14 * this.subcomponents.size() + 13 + 3;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 11 + this.offset;
    }
}
