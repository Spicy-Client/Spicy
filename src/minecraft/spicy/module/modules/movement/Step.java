package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.network.play.client.C03PacketPlayer;
import spicy.events.game.SendPacketEvent;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.events.player.PlayerMoveEvent;
import spicy.events.player.PlayerStepEvent;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.settings.Setting;
import spicy.utils.TimeHelper;

public class Step extends Module
{
    private boolean offset;
    private TimeHelper time;

    public Step() {
        super("Step", 0, Category.MOVEMENT, true);
        this.offset = false;
        this.time = new TimeHelper();
    }


    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("height", this, 1.0, 0.5, 5.0, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("reverse", this, false));
        super.setup();
    }

    @Override
    public void onDisabled() {
        this.mc.thePlayer.stepHeight = 0.5f;
        super.onDisabled();
    }

    @SubscribeEvent
    public void onUpdate(final PlayerMotionUpdateEvent _event) {
        final Speed speed = (Speed) ModuleManager.getModule(Speed.class);
        final boolean checks = this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && this.time.hasReached(5L);
        if (checks) {
            this.mc.thePlayer.stepHeight = (float)Spicy.INSTANCE.settingsManager.getSettingByName("height").getValDouble();
        }
        else {
            this.mc.thePlayer.stepHeight = 0.5f;
        }
    }

    @SubscribeEvent
    public void onPreMovePlayer(final PlayerMoveEvent event) {
        if (Spicy.INSTANCE.settingsManager.getSettingByName("reverse").getValBoolean() && !this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0 && this.mc.thePlayer.fallDistance < 0.2 && !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0)).isEmpty()) {
            event.setY(event.getY() - 1.0);
        }
    }

    @SubscribeEvent
    public void onStep(final PlayerStepEvent event) {
        if (this.mc.thePlayer.stepHeight == Spicy.INSTANCE.settingsManager.getSettingByName("height").getValDouble()) {
            this.time.reset();
            this.offset = true;
        }
    }

    @SubscribeEvent
    public void onSendPacket(final SendPacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer && this.offset) {
            final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)event.getPacket();
            c03PacketPlayer.y += 0.07;
            this.offset = false;
        }
    }

    @Override
    public String getModuleDesc() {
        return "Step up blocks one at a time.";
    }
}
