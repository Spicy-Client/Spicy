package spicy.module.modules.player;

import com.darkmagician6.eventapi.SubscribeEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import spicy.events.entity.EntityBlockCullEvent;
import spicy.events.entity.EntityBoundingBoxEvent;
import spicy.events.entity.EntityInsideBlockRenderEvent;
import spicy.events.entity.EntityPushOutOfBlocksEvent;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.events.player.PlayerMoveEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

import java.util.UUID;

public class Freecam extends Module {
    private EntityOtherPlayerMP freecamEntity;
    private double speed;

    public Freecam() {
        super("Freecam", 0, Category.PLAYER);
        this.speed = 1;
    }



    @Override
    public void onEnabled() {
        System.out.println("Freecam enabled.");
        if(mc.thePlayer == null) {
            return;
        }
        this.freecamEntity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69L, 96L), "Freecam"));
        this.freecamEntity.inventory = mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        mc.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
        mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
        mc.renderGlobal.loadRenderers();
        System.out.println("Freecam disabled.");
        super.onDisabled();
    }

    @Override
    public String getModuleDesc() {
        return "Allows you move free";
    }

    @SubscribeEvent
    public void onUpdate(PlayerMotionUpdateEvent event) {
        if(event.getState() == PlayerMotionUpdateEvent.State.PRE) {
            event.setCancelled(true);
        }
    }

    @SubscribeEvent
    private void onMove(final PlayerMoveEvent event) {
        if (mc.thePlayer.movementInput.jump) {
            event.setY(mc.thePlayer.motionY = this.speed);
        }
        else if (mc.thePlayer.movementInput.sneak) {
            event.setY(mc.thePlayer.motionY = -this.speed);
        }
        else {
            event.setY(mc.thePlayer.motionY = 0.0);
        }
        Wrapper.setMoveSpeed(event, this.speed);
    }

    @SubscribeEvent
    private void onBoundingBox(final EntityBoundingBoxEvent event) {
        event.setBoundingBox(null);
    }

    @SubscribeEvent
    private void onPushOutOfBlocks(final EntityPushOutOfBlocksEvent event) {
        event.setCancelled(true);
    }

    @SubscribeEvent
    private void onInsideBlockRender(final EntityInsideBlockRenderEvent event) {
        event.setCancelled(true);
    }

    @SubscribeEvent
    private void onBlockCull(final EntityBlockCullEvent event) {
        event.setCancelled(true);
    }


}
