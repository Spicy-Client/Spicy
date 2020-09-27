package spicy.notification;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import spicy.main.Spicy;
import spicy.utils.AnimationUtils;
import spicy.utils.RenderUtil;
import spicy.utils.Translate;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationPublisher
{
    public static final List<Notification> NOTIFICATIONS;

    public static void publish(final ScaledResolution sr) {
        if (NotificationPublisher.NOTIFICATIONS.isEmpty()) {
            return;
        }
        final int srScaledHeight = sr.getScaledHeight();
        final int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 30;
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr =  mc.fontRendererObj;
        for (final Notification notification : NotificationPublisher.NOTIFICATIONS) {
            final Translate translate = notification.getTranslate();
            final int width = notification.getWidth();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtils.animate(width, notification.scissorBoxWidth, 0.1);
                translate.interpolate(scaledWidth - width, y, 0.15);
            }
            else {
                notification.scissorBoxWidth = AnimationUtils.animate(0.0, notification.scissorBoxWidth, 0.1);
                if (notification.scissorBoxWidth < 1.0) {
                    NotificationPublisher.NOTIFICATIONS.remove(notification);
                }
                y += 30;
            }
            final float translateX = (float)translate.getX();
            final float translateY = (float)translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.prepareScissorBox((float)(scaledWidth - notification.scissorBoxWidth), translateY, (float)scaledWidth, translateY + 30.0f);
            Gui.drawRect(translateX, translateY, scaledWidth, translateY + 30.0f, -1879048192);
            Gui.drawRect(translateX, translateY + 30.0f - 2.0f, translateX + width * ((notification.getTime() - notification.getTimer().getElapsedTime()) / (double)notification.getTime()), translateY + 30.0f, notification.getType().getColor());
            Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow(notification.getTitle(), translateX + 4.0f, translateY + 4.0f, -1);
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(notification.getContent(), translateX + 4.0f, translateY + 17.0f, Color.lightGray.hashCode());
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            y -= 33;
        }
    }

    public static void queue(final String title, final String content, final NotificationType type) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRendererObj;
            NotificationPublisher.NOTIFICATIONS.add(new Notification(title, content, type, fr));
    }

    static {
        NOTIFICATIONS = new CopyOnWriteArrayList<Notification>();
    }
}
