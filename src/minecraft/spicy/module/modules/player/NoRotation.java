package spicy.module.modules.player;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import spicy.events.game.TakePacketEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

public class NoRotation extends Module {

    public NoRotation() {
        super("NoRotation", 0, Category.PLAYER);
    }

    @SubscribeEvent
    public void onPacketTake(TakePacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            packet.yaw = Wrapper.yaw();
            packet.pitch = Wrapper.pitch();
        }
    }

    @Override
    public String getModuleDesc() {
        return "No head rotations";
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}

