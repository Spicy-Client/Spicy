package spicy.events.game;

import com.darkmagician6.eventapi.events.*;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class KeyPressEvent implements Event
{
    private int key;

    public KeyPressEvent(final int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(final int key) {
        this.key = key;
    }
}
