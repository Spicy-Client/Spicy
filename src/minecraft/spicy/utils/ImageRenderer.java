package spicy.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageRenderer
{
    public static final ImageRenderer GENERIC_RENDERER;
    private String directory;
    private Map<String, ResourceLocation> resources;
    
    public ImageRenderer(final String directory) {
        this.directory = directory;
        this.resources = new HashMap<String, ResourceLocation>();
    }
    
    public ImageRenderer(final ImageRenderer base, final String sub) {
        this(base.directory + File.pathSeparator + sub);
    }
    
    public void renderImage(final String fileName, final double startX, final double startY, final double width, final double height) {
        final String fullFileName = fileName + ".png";
        ResourceLocation resourceLocation = this.resources.get(fullFileName);
        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(String.format("textures/energetic/%s/%s", this.directory, fullFileName));
            this.resources.put(fullFileName, resourceLocation);
        }
        this.renderImage(resourceLocation, startX, startY, width, height);
    }
    
    public void renderImage(final ResourceLocation resourceLocation, final double startX, final double startY, final double width, final double height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        final double scaleX = width / 256.0;
        final double scaleY = height / 256.0;
        if (scaleX <= 0.0 || scaleY <= 0.0) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(startX, startY, 0.0);
        GL11.glScaled(scaleX, scaleY, 1.0);
        Gui.INSTANCE.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        GL11.glScaled(1.0 / scaleX, 1.0 / scaleY, 1.0);
        GL11.glPopMatrix();
    }
    
    static {
        GENERIC_RENDERER = new ImageRenderer("/");
    }
}
