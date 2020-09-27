package spicy.utils;

import net.minecraft.client.gui.Gui;

public class Render
{
    public static void drawRectWH(final double x, final double y, final double width, final double height, final int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }
    
    public static void drawRect(final double x, final double y, final double x2, final double y2, final int color) {
        Gui.drawRect(x, y, x2, y2, color);
    }
    
    public static void drawBorderedRectWithoutATop(final double x, final double y, final double x2, final double y2, final double thickness, final int inside, final int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        drawRect(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        drawRect(x, y + 1.0 - fix, x + thickness, y2, outline);
        drawRect(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        drawRect(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double y2, final double thickness, final int inside, final int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        drawRect(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        drawRect(x, y + 1.0 - fix, x + thickness, y2, outline);
        drawRect(x, y, x2 - 1.0 + fix, y + thickness, outline);
        drawRect(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        drawRect(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }
}
