package spicy.gui.alt;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import spicy.alt.Alt;
import spicy.alt.LoginThread;
import spicy.main.Spicy;
import spicy.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GuiAltManager extends GuiScreen
{
    private GuiScreen parent;
    private GuiButton login;
    private GuiButton remove;
    private GuiButton last;
    private int offset;
    public Alt selected;
    public int index;
    private GuiTextField searchBar;
    private LoginThread loginThread;
    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe
    public GuiAltManager(final GuiScreen parent) {
        this.parent = parent;
        try {
            this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
        this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
        this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
        this.buttonList.add(this.last = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Last Alt"));
        this.searchBar = new GuiTextField(0, this.mc.fontRendererObj, this.width / 2 - 100, 14, 200, 16);
        this.login.enabled = false;
        this.remove.enabled = false;
        this.last.enabled = false;
        if (!Spicy.INSTANCE.altManager.getAlts().isEmpty()) {
            this.index = 0;
            this.selected = Spicy.INSTANCE.altManager.getAlts().get(this.index);
        }
        else {
            this.index = 0;
            this.selected = null;
        }
        initTime = System.currentTimeMillis();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final ArrayList<Alt> alts = (ArrayList<Alt>)Spicy.INSTANCE.altManager.getAlts().clone();
        if (!this.searchBar.getText().isEmpty()) {
            alts.clear();
            for (final Alt alt : Spicy.INSTANCE.altManager.getAlts()) {
                if (alt.getUsername().toLowerCase().contains(this.searchBar.getText().toLowerCase())) {
                    alts.add(alt);
                }
            }
        }
        this.searchBar.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset;
        for (final Alt alt2 : alts) {
            if (this.isMouseOverAlt(mouseX, mouseY, y)) {
                if (alt2 == this.selected) {
                    this.actionPerformed(this.buttonList.get(1));
                    return;
                }
                this.selected = alt2;
                this.index = Spicy.INSTANCE.altManager.getAlts().indexOf(alt2);
            }
            y += 26;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null || !this.loginThread.getStatus().contains("Logging in")) {
                    this.mc.displayGuiScreen(this.parent);
                    break;
                }
                break;
            }
            case 1: {
                final String user = this.selected.getUsername();
                final String pass = this.selected.getPassword();
                (this.loginThread = new LoginThread(user, pass)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                Spicy.INSTANCE.altManager.getAlts().remove(this.selected);
                break;
            }
            case 3: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                try {
                    final Random rand = new Random();
                    final Alt randomAlt = Spicy.INSTANCE.altManager.getAlts().get(rand.nextInt(Spicy.INSTANCE.altManager.getAlts().size() - 1));
                    (this.loginThread = new LoginThread(randomAlt.getUsername(), randomAlt.getPassword())).start();
                }
                catch (Exception ex) {}
                break;
            }
            case 6: {
                (this.loginThread = new LoginThread(Spicy.INSTANCE.altManager.getLastAlt().getUsername(), Spicy.INSTANCE.altManager.getLastAlt().getPassword())).start();
                break;
            }
        }
    }
    
    public void keyTyped(final char character, final int keyCode) {
        this.searchBar.textboxKeyTyped(character, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 13;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 13;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
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
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString("Account Manager - " + Spicy.INSTANCE.altManager.getAlts().size() + ((Spicy.INSTANCE.altManager.getAlts().size() == 1) ? " alt." : " alts."), this.width / 2, 2, -1);
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawString((this.loginThread == null) ? "Idle..." : this.loginThread.getStatus(), 10, 2, -1);
        Spicy.INSTANCE.fontManager.getFont("FONT 15").drawString(this.mc.session.getUsername(), 10, 12, -7829368);
        RenderHelper.drawBorderedRect(50.0f, 33.0f, (float)(this.width - 50), (float)(this.height - 50), 1.0f, -16777216, Integer.MIN_VALUE);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, (float)this.width, (float)(this.height - 50));
        GL11.glEnable(3089);
        final ArrayList<Alt> alts = (ArrayList<Alt>)Spicy.INSTANCE.altManager.getAlts().clone();
        if (!this.searchBar.getText().isEmpty()) {
            alts.clear();
            for (final Alt alt : Spicy.INSTANCE.altManager.getAlts()) {
                if (alt.getUsername().toLowerCase().contains(this.searchBar.getText().toLowerCase())) {
                    alts.add(alt);
                }
            }
        }
        int y = 38;
        for (final Alt alt2 : alts) {
            if (!this.isAltInArea(y)) {
                continue;
            }
            if (alt2 == this.selected) {
                if (this.isMouseOverAlt(mouseX, mouseY, y - this.offset) && Mouse.isButtonDown(0)) {
                    RenderHelper.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2142943931);
                }
                else if (this.isMouseOverAlt(mouseX, mouseY, y - this.offset)) {
                    RenderHelper.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2142088622);
                }
                else {
                    RenderHelper.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2144259791);
                }
            }
            else if (this.isMouseOverAlt(mouseX, mouseY, y - this.offset) && Mouse.isButtonDown(0)) {
                RenderHelper.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2146101995);
            }
            else if (this.isMouseOverAlt(mouseX, mouseY, y - this.offset)) {
                RenderHelper.drawBorderedRect(52.0f, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0f, -16777216, -2145180893);
            }
            if (alt2.getUsername().contains("@")) {
                Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString(alt2.getDisplay() + " (" + alt2.getUsername() + ")", this.width / 2, y - this.offset, -1);
            }
            else {
                Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString(alt2.getUsername(), this.width / 2, y - this.offset, -1);
            }
            GlStateManager.color(1,1,1);
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString(alt2.getPassword().isEmpty() ? (ChatColor.RED + "Cracked") : alt2.getPassword().replaceAll(".", "*"), this.width / 2, y - this.offset + 10, -1);
            GlStateManager.color(1,1,1);
            final PlayerSkin skin = PlayerSkinCache.getInstance().getPlayerSkin(alt2.getDisplay());
            skin.renderComponent(SkinComponent.HEAD, 60, y - this.offset - 3, 22, 22);
            skin.renderComponent(SkinComponent.HAT, 60, y - this.offset - 3, 22, 22);
            y += 26;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.selected == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
        }
        this.last.enabled = (Spicy.INSTANCE.altManager.getLastAlt() != null);
        this.searchBar.drawTextBox();
    }
    
    public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final int factor = new ScaledResolution(this.mc).getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((new ScaledResolution(this.mc).getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= this.height - 50;
    }
    
    private boolean isMouseOverAlt(final int x, final int y, final int y1) {
        return x >= 52 && y >= y1 - 4 && x <= this.width - 52 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width && y <= this.height - 50;
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
