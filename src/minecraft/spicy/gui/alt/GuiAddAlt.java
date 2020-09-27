package spicy.gui.alt;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import spicy.alt.Alt;
import spicy.alt.GuiMaskedTextField;
import spicy.main.Spicy;

import java.io.IOException;

public class GuiAddAlt extends GuiScreen
{
    private GuiTextField username;
    private GuiMaskedTextField password;
    private GuiScreen parent;
    
    public GuiAddAlt(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        (this.username = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.username.setFocused(true);
        (this.password = new GuiMaskedTextField(0, this.fontRendererObj, this.width / 2 - 100, 100, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
    }
    
    public void keyTyped(final char character, final int keyCode) {
        this.username.textboxKeyTyped(character, keyCode);
        this.password.textboxKeyTyped(character, keyCode);
        if (keyCode == 15) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (keyCode == 28) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString("Add Alt", this.width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawString("Username / E-Mail", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawString("Password", this.width / 2 - 96, 106, -7829368);
        }
        this.username.drawTextBox();
        this.password.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                if (!this.username.getText().isEmpty()) {
                    final Alt alt = new Alt(Spicy.INSTANCE.altManager.resolveName(new Alt(this.username.getText(), this.password.getText())), this.username.getText(), this.password.getText());
                    if (this.parent instanceof GuiAltManager) {
                        final GuiAltManager manager = (GuiAltManager)this.parent;
                        if (manager.selected != null) {
                            Spicy.INSTANCE.altManager.getAlts().add(manager.index + 1, alt);
                        }
                        else {
                            Spicy.INSTANCE.altManager.getAlts().add(alt);
                        }
                    }
                    else {
                        Spicy.INSTANCE.altManager.getAlts().add(alt);
                    }
                }
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}
