package spicy.module.modules.combat;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.settings.Setting;

public class Regen extends Module
{

    public Regen() {
        super("Regen",0, Category.COMBAT);
    }

    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("packets", this, 3000.0, 0, 6000.0, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("potion-packets", this, 60.0, 0, 200.0, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("health", this, 8.0, 0, 10.0, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("potion", this, true));
        super.setup();
    }

    @SubscribeEvent
    private void onUpdate(final PlayerMotionUpdateEvent event) {
        if (Spicy.INSTANCE.settingsManager.getSettingByName("potion").getValBoolean()) {
            if (Wrapper.player().getActivePotionEffect(Potion.regeneration) != null && Wrapper.player().getActivePotionEffect(Potion.regeneration).getDuration() > 0 && Wrapper.player().getHealth() <= Spicy.INSTANCE.settingsManager.getSettingByName("health").getValDouble() * 2.0 && (Wrapper.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(PlayerMotionUpdateEvent.State.POST)) {
                for (int i = 0; i < Spicy.INSTANCE.settingsManager.getSettingByName("potion-packets").getValDouble(); ++i) {
                    Wrapper.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
                    Wrapper.packet(new C03PacketPlayer(true));
                }
            }
        }
        else if (Wrapper.player().getHealth() <= Spicy.INSTANCE.settingsManager.getSettingByName("health").getValDouble() * 2.0 && (Wrapper.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(PlayerMotionUpdateEvent.State.POST)) {
            for (int i = 0; i < Spicy.INSTANCE.settingsManager.getSettingByName("packets").getValDouble(); ++i) {
                Wrapper.packet(new C03PacketPlayer(true));
            }
        }
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Wrapper.player().boundingBox.minX); x < MathHelper.floor_double(Wrapper.player().boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Wrapper.player().boundingBox.minY); y < MathHelper.floor_double(Wrapper.player().boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Wrapper.player().boundingBox.minZ); z < MathHelper.floor_double(Wrapper.player().boundingBox.maxZ) + 1; ++z) {
                    final Block block = Wrapper.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Wrapper.world(), new BlockPos(x, y, z), Wrapper.world().getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Wrapper.player().boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Auto regen health";
    }
}
