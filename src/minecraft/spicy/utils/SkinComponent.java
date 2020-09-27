package spicy.utils;

import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import java.awt.geom.*;
import java.awt.image.*;

public enum SkinComponent
{
    HEAD(8, 8, 8, 8, false), 
    HAT(40, 8, 8, 8, false), 
    BODY(20, 20, 8, 12, false), 
    LEFT_LEG(4, 20, 4, 12, true), 
    LEFT_ARM(44, 20, 4, 12, true), 
    RIGHT_LEG(4, 20, 4, 12, false), 
    RIGHT_ARM(44, 20, 4, 12, false);
    
    private int posX;
    private int posY;
    private int width;
    private int height;
    private boolean flip;
    
    private SkinComponent(final int posX, final int posY, final int width, final int height, final boolean flip) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.flip = flip;
    }
    
    public ResourceLocation crop(final BufferedImage bufferImage, final String resourceName) {
        BufferedImage cropped = bufferImage.getSubimage(this.posX, this.posY, this.width, this.height);
        if (this.flip) {
            cropped = this.flip(cropped);
        }
        return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(String.format(resourceName, this.name()), new DynamicTexture(cropped));
    }
    
    private BufferedImage flip(final BufferedImage bufferedImage) {
        final AffineTransform transform = AffineTransform.getScaleInstance(-1.0, 1.0);
        transform.translate(-bufferedImage.getWidth(), 0.0);
        final AffineTransformOp flip = new AffineTransformOp(transform, 1);
        return flip.filter(bufferedImage, null);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
