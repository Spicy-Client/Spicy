package minimap.events;

import com.darkmagician6.eventapi.SubscribeEvent;
import minimap.XaeroMinimap;
import minimap.minimap.Minimap;
import minimap.settings.ModSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import spicy.events.game.GameTickEvent;
import spicy.events.game.KeyPressEvent;

import java.util.ArrayList;

public class FMLEvents
{
    public ArrayList<KeyEvent> keyEvents;
    public ArrayList<KeyEvent> oldKeyEvents;
    
    public FMLEvents() {
        this.keyEvents = new ArrayList<KeyEvent>();
        this.oldKeyEvents = new ArrayList<KeyEvent>();
       // EventManager.register(this);
    }
    
    private boolean eventExists(final KeyBinding kb) {
        for (final KeyEvent o : this.keyEvents) {
            if (o.kb == kb) {
                return true;
            }
        }
        return this.oldEventExists(kb);
    }
    
    private boolean oldEventExists(final KeyBinding kb) {
        for (final KeyEvent o : this.oldKeyEvents) {
            if (o.kb == kb) {
                return true;
            }
        }
        return false;
    }
    
    @SubscribeEvent
    public void playerTick(GameTickEvent event) {
        //if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
            if (XaeroMinimap.getSettings() != null && (XaeroMinimap.getSettings().getDeathpoints() || XaeroMinimap.getSettings().getShowWaypoints() || XaeroMinimap.getSettings().getShowIngameWaypoints())) {
                Minimap.updateWaypoints();
            }
           /* else if (Minimap.waypoints != null) {
                Minimap.waypoints = null;
            }*/
            final Minecraft mc = XaeroMinimap.mc;
            for (int i = 0; i < this.keyEvents.size(); ++i) {
                final KeyEvent ke = this.keyEvents.get(i);
                if (mc.currentScreen == null) {
                    XaeroMinimap.ch.keyDown(ke.kb, ke.tickEnd, ke.isRepeat);
                }
                if (!ke.isRepeat) {
                    if (!this.oldEventExists(ke.kb)) {
                        this.oldKeyEvents.add(ke);
                    }
                    this.keyEvents.remove(i);
                    --i;
                }
                else if (!ControlsHandler.isDown(ke.kb)) {
                    XaeroMinimap.ch.keyUp(ke.kb, ke.tickEnd);
                    this.keyEvents.remove(i);
                    --i;
                }
            }
            for (int i = 0; i < this.oldKeyEvents.size(); ++i) {
                final KeyEvent ke = this.oldKeyEvents.get(i);
                if (!ControlsHandler.isDown(ke.kb)) {
                    XaeroMinimap.ch.keyUp(ke.kb, ke.tickEnd);
                    this.oldKeyEvents.remove(i);
                    --i;
                }
            }
      //  }
    }
    
    @SubscribeEvent
    public void keyInput(KeyPressEvent event) {
        if (XaeroMinimap.mc.thePlayer != null) {
            final Minecraft mc = XaeroMinimap.mc;
            if (mc.currentScreen == null) {
                for (int i = 0; i < mc.gameSettings.keyBindings.length; ++i) {
                    try {
                        if (mc.currentScreen == null && !this.eventExists(mc.gameSettings.keyBindings[i]) && ControlsHandler.isDown(mc.gameSettings.keyBindings[i])) {
                            this.keyEvents.add(new KeyEvent(mc.gameSettings.keyBindings[i], false, ModSettings.isKeyRepeat(mc.gameSettings.keyBindings[i]), true));
                        }
                    }
                    catch (Exception ex) {}
                }
            }
        }
    }
}
