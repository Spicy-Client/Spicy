package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.events.player.PlayerMoveEvent;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.settings.Setting;

public class Fly extends Module
{

    public Fly() {
        super("Fly", 0, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("speed", this, 1, 0, 10.0, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("glide-speed", this, 0.05, 0.005, 1.0, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("nc", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("damage", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("glide", this, false));
        super.setup();
    }

    @Override
    public void onEnabled() {
        if (Spicy.INSTANCE.settingsManager.getSettingByName("damage").getValBoolean() && Wrapper.mc().thePlayer.isCollidedVertically) {
            for (int i = 0; i < 80.0; ++i) {
                Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc().thePlayer.posX, Wrapper.mc().thePlayer.posY + 0.049, Wrapper.mc().thePlayer.posZ, false));
                Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc().thePlayer.posX, Wrapper.mc().thePlayer.posY, Wrapper.mc().thePlayer.posZ, false));
            }
            Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc().thePlayer.posX, Wrapper.mc().thePlayer.posY, Wrapper.mc().thePlayer.posZ, true));
        }
        Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc().thePlayer.posX, Wrapper.mc().thePlayer.posY + 0.01, Wrapper.mc().thePlayer.posZ, false));
        super.onEnabled();
    }
    
    @SubscribeEvent
    private void onUpdate(final PlayerMotionUpdateEvent event) {
        if (event.getState() == PlayerMotionUpdateEvent.State.PRE) {
            if (Spicy.INSTANCE.settingsManager.getSettingByName("nc").getValBoolean()) {
                if (!Wrapper.mc().thePlayer.movementInput.jump && !Wrapper.mc().thePlayer.movementInput.sneak && Spicy.INSTANCE.settingsManager.getSettingByName("glide").getValBoolean()) {
                    final MovementInput movementInput = Wrapper.mc().thePlayer.movementInput;
                    if (Wrapper.mc().thePlayer.moveForward == 0.0) {
                        final MovementInput movementInput2 = Wrapper.mc().thePlayer.movementInput;
                        if (Wrapper.mc().thePlayer.moveStrafing == 0.0) {
                            final EntityPlayerSP thePlayer = Wrapper.mc().thePlayer;
                            final EntityPlayerSP thePlayer2 = Wrapper.mc().thePlayer;
                            final EntityPlayerSP thePlayer3 = Wrapper.mc().thePlayer;
                            final double motionX = 0.0;
                            thePlayer3.motionY = 0.0;
                            thePlayer2.motionZ = 0.0;
                            thePlayer.motionX = 0.0;
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                if (Wrapper.mc().thePlayer.movementInput.jump) {
                    Wrapper.mc().thePlayer.motionY = Spicy.INSTANCE.settingsManager.getSettingByName("speed").getValDouble();
                }
                else if (Wrapper.mc().thePlayer.movementInput.sneak) {
                    Wrapper.mc().thePlayer.motionY = -Spicy.INSTANCE.settingsManager.getSettingByName("speed").getValDouble();
                }
                else if (Spicy.INSTANCE.settingsManager.getSettingByName("glide").getValBoolean()) {
                    Wrapper.mc().thePlayer.motionY = -Spicy.INSTANCE.settingsManager.getSettingByName("glide-speed").getValDouble();
                }
                else {
                    Wrapper.mc().thePlayer.motionY = 0.0;
                }
            }
            else if (Wrapper.mc().thePlayer.movementInput.jump) {
                Wrapper.mc().thePlayer.motionY = Spicy.INSTANCE.settingsManager.getSettingByName("speed").getValDouble() / 2.0;
            }
            else if (Wrapper.mc().thePlayer.movementInput.sneak) {
                Wrapper.mc().thePlayer.motionY = -Spicy.INSTANCE.settingsManager.getSettingByName("speed").getValDouble() / 2.0;
            }
            else if (Spicy.INSTANCE.settingsManager.getSettingByName("glide").getValBoolean()) {
                Wrapper.mc().thePlayer.motionY = -Spicy.INSTANCE.settingsManager.getSettingByName("glide-speed").getValDouble();
            }
            else {
                Wrapper.mc().thePlayer.motionY = 0.0;
            }
        }
    }
    
    @SubscribeEvent
    private void onMove(final PlayerMoveEvent moveEvent) {
    }





    @Override
    public String getModuleDesc() {
        return "Allows you fly";
    }
}
