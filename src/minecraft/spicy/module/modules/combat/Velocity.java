package spicy.module.modules.combat;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import spicy.events.game.TakePacketEvent;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.settings.Setting;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class Velocity extends Module {

    public Velocity() {
        super("Velocity", 0, Category.COMBAT, true);
    }

    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("Percent", this, 0, 0, 20.0, true));
        super.setup();
    }

    @Override
    public void onEnabled() {
        System.out.println("Velocity enabled.");
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("Velocity disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "No knockback";
    }

    @SubscribeEvent
    private void onPacketReceive(final TakePacketEvent event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
            if (mc.theWorld.getEntityByID(packet.getEntityID()) == mc.thePlayer) {
                if (Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() > 0.0) {
                    final S12PacketEntityVelocity s12PacketEntityVelocity = packet;
                    s12PacketEntityVelocity.motionX *= (int)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
                    final S12PacketEntityVelocity s12PacketEntityVelocity2 = packet;
                    s12PacketEntityVelocity2.motionY *= (int)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
                    final S12PacketEntityVelocity s12PacketEntityVelocity3 = packet;
                    s12PacketEntityVelocity3.motionZ *= (int)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
                }
                else {
                    event.setCancelled(true);
                }
            }
        }
        else if (event.getPacket() instanceof S27PacketExplosion) {
            final S27PacketExplosion s27PacketExplosion;
            final S27PacketExplosion packet2 = s27PacketExplosion = (S27PacketExplosion)event.getPacket();
            s27PacketExplosion.posX *= (float)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
            final S27PacketExplosion s27PacketExplosion2 = packet2;
            s27PacketExplosion2.posY *= (float)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
            final S27PacketExplosion s27PacketExplosion3 = packet2;
            s27PacketExplosion3.posZ *= (float)(Spicy.INSTANCE.settingsManager.getSettingByName("Percent").getValDouble() / 100.0);
        }
    }
}

