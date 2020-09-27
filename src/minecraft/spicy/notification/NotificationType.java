package spicy.notification;

import java.awt.*;

public enum NotificationType
{
    ENABLE(new Color(0x60FF00).getRGB()),
    DISABLE(new Color(0xFF0000).getRGB()),
    WARNING(new Color(0xFF6E00).getRGB());

    private final int color;

    private NotificationType(final int color) {
        this.color = color;
    }

    public final int getColor() {
        return this.color;
    }
}
