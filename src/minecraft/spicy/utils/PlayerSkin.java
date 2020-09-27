package spicy.utils;

import net.minecraft.util.*;
import java.util.*;
import java.awt.image.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class PlayerSkin
{
    private static final double SKIN_HEIGHT = 32.0;
    private Map<SkinComponent, ResourceLocation> skinLocations;
    private boolean skinLoaded;
    
    public PlayerSkin() {
        this.skinLocations = new HashMap<SkinComponent, ResourceLocation>();
    }
    
    public void setPlayerSkin(final BufferedImage skin, final String resourceLocation) {
        for (final SkinComponent skinComponent : SkinComponent.values()) {
            this.skinLocations.put(skinComponent, skinComponent.crop(skin, resourceLocation));
        }
        this.skinLoaded = true;
    }
    
    public void renderComponent(final SkinComponent skinComponent, final int posX, final int posY, final int width, final int height) {
        if (this.skinLoaded) {
            GL11.glColor3d(1.0, 1.0, 1.0);
            final ResourceLocation skinLocation = this.skinLocations.get(skinComponent);
            ImageRenderer.GENERIC_RENDERER.renderImage(skinLocation, posX, posY, width, height);
        }
        else if (skinComponent != SkinComponent.HAT) {
            Gui.drawRect(posX, posY, posX + width, posY + height, 5263440);
        }
    }
    
    private void renderComponent(final SkinComponent skinComponent, final int posX, final int posY) {
        this.renderComponent(skinComponent, posX, posY, skinComponent.getWidth(), skinComponent.getHeight());
    }
    
    public void renderSkin(final double posX, final double posY, final double height) {
        final double scale = height / 32.0;
        GL11.glPushMatrix();
        GL11.glTranslated(posX, posY, 0.0);
        GL11.glScaled(scale, scale, 1.0);
        this.renderComponent(SkinComponent.HEAD, 4, 0);
        this.renderComponent(SkinComponent.HAT, 4, 0);
        this.renderComponent(SkinComponent.LEFT_ARM, 0, 8);
        this.renderComponent(SkinComponent.RIGHT_ARM, 12, 8);
        this.renderComponent(SkinComponent.BODY, 4, 8);
        this.renderComponent(SkinComponent.LEFT_LEG, 8, 20);
        this.renderComponent(SkinComponent.RIGHT_LEG, 4, 20);
        GL11.glPopMatrix();
    }
    
    public boolean isSkinLoaded() {
        return this.skinLoaded;
    }
}
