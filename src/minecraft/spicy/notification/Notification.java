package spicy.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import spicy.main.Spicy;
import spicy.utils.Stopwatch;
import spicy.utils.Translate;

public final class Notification
{
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final Stopwatch timer;
    private final Translate translate;
    private final FontRenderer fontRenderer;
    public double scissorBoxWidth;

    public Notification(final String title, final String content, final NotificationType type, final FontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = 2500;
        this.type = type;
        this.timer = new Stopwatch();
        this.fontRenderer = fontRenderer;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate((float)(sr.getScaledWidth() - this.getWidth()), (float)(sr.getScaledHeight() - 30));
    }

    public final int getWidth() {
        return (int) Math.max(100, Math.max(Spicy.INSTANCE.fontManager.getFont("FONT 20").getWidth(this.title), Spicy.INSTANCE.fontManager.getFont("FONT 15").getWidth(this.content)) + 10);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }

    public final NotificationType getType() {
        return this.type;
    }

    public final Stopwatch getTimer() {
        return this.timer;
    }

    public final Translate getTranslate() {
        return this.translate;
    }
}
