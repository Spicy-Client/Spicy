package spicy.module.modules.player;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import spicy.events.entity.EntityBoundingBoxEvent;
import spicy.events.entity.EntityInsideBlockRenderEvent;
import spicy.events.entity.EntityPushOutOfBlocksEvent;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.utils.Timer;

public class NCPhase extends Module
{
    private Timer timer;
    
    public NCPhase() {
        super("NCPhase",0, Category.PLAYER);
        this.timer = new Timer();
    }
    
    @Override
    public void onEnabled() {
        this.timer.reset();
        super.onEnabled();
    }
    
    @SubscribeEvent
    public void onPost(final PlayerMotionUpdateEvent event) {
        if (event.getState().equals(PlayerMotionUpdateEvent.State.POST) && Wrapper.player().isCollidedHorizontally && !Wrapper.player().isOnLadder()) {
            double xOff = 0.0;
            double zOff = 0.0;
            final double multiplier = 0.3;
            final double mx = Math.cos(Math.toRadians(Wrapper.yaw() + 90.0f));
            final double mz = Math.sin(Math.toRadians(Wrapper.yaw() + 90.0f));
            xOff = Wrapper.movementInput().moveForward * 0.3 * mx + Wrapper.movementInput().moveStrafe * 0.3 * mz;
            zOff = Wrapper.movementInput().moveForward * 0.3 * mz - Wrapper.movementInput().moveStrafe * 0.3 * mx;
            Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.x() + xOff, Wrapper.y(), Wrapper.z() + zOff, false));
            for (int i = 1; i < 10; ++i) {
                Wrapper.packet(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.x(), 8.988465674311579E307, Wrapper.z(), false));
            }
            Wrapper.player().setPosition(Wrapper.x() + xOff, Wrapper.y(), Wrapper.z() + zOff);
        }
    }
    
    @SubscribeEvent
    public void onBB(final EntityBoundingBoxEvent event) {
        if (this.isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > Wrapper.player().boundingBox.minY) {
            event.setBoundingBox(null);
        }
    }
    
    @SubscribeEvent
    public void onPush(final EntityPushOutOfBlocksEvent event) {
        event.setCancelled(true);
    }
    
    @SubscribeEvent
    private void onInsideBlockRender(final EntityInsideBlockRenderEvent event) {
        event.setCancelled(true);
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
    public void onDisabled() {
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Allows you go throught blocks";
    }
}
