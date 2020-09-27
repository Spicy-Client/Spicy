package spicy.module.modules.combat;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.events.render.Render3DEvent;
import spicy.friend.FriendManager;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.module.modules.movement.NoSlowdown;
import spicy.module.modules.movement.Speed;
import spicy.notification.NotificationPublisher;
import spicy.notification.NotificationType;
import spicy.settings.Setting;
import spicy.utils.GLUtils;
import spicy.utils.RotationUtils;
import spicy.utils.StateManager;
import spicy.utils.Timer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {
    public double ticksExisted;
    private boolean setupTick;
    private boolean switchingTargets;
    public List<EntityLivingBase> targets;
    public int index;
    private Timer timer;
    public static boolean attacking = false;
    private int test;
    private float animtest;
    private boolean anim;
    public boolean canSendNotification;


    public KillAura() {
        super("KillAura", 0, Category.COMBAT);
        this.ticksExisted = 10.0;
        this.timer = new Timer();
        this.targets = new ArrayList<EntityLivingBase>();
    }


    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Switch");
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("AuraMode", this, "Switch", modes));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("reach", this, 3, 0, 7, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("players", this, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("entities", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("noslowdown", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("auto-block", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("criticals", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("head-rotations", this, false));
        super.setup();
    }

    private void drawCircle(final Entity entity, final float partialTicks, final double rad) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GLUtils.startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(0.01f);
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
        final float r = 0.003921569f * Color.WHITE.getRed();
        final float g = 0.003921569f * Color.WHITE.getGreen();
        final float b = 0.003921569f * Color.WHITE.getBlue();
        if (this.test <= 10) {
            if (this.anim) {
                this.animtest += 0.01F;
            } else {
                this.animtest -= 0.01F;
            }
            this.test = 10;
        }
        this.test--;
        if (this.animtest <= y) {
            this.anim = true;
        } else if (this.animtest >= y + entity.getEyeHeight() + 0.25) {
            this.anim = false;
        }
            for (int i = 0; i <= 90; ++i) {
                GL11.glColor4f(r, g, b,i);
                GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), animtest, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GLUtils.endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }


    @SubscribeEvent
    public void onRender(Render3DEvent event) {
        this.drawCircle(targets.get(index), event.getPartialTicks(), 0.49);
    }




    @SubscribeEvent
    public void onPre(PlayerMotionUpdateEvent event) {
        if(event.getState() == PlayerMotionUpdateEvent.State.PRE) {
            this.setModuleSuffix(Spicy.INSTANCE.settingsManager.getSettingByName("AuraMode").getValString());
            final String valString = Spicy.INSTANCE.settingsManager.getSettingByName("AuraMode").getValString();
            switch (valString) {
                case "Switch": {
                    StateManager.setOffsetLastPacketAura(false);
                    final NoSlowdown noSlowdownModule = (NoSlowdown) ModuleManager.getModule(NoSlowdown.class);
                    this.targets = this.getTargets();
                    if (this.index >= this.targets.size()) {
                        this.index = 0;
                    }
                    if (this.targets.size() > 0) {
                        if (!canSendNotification) {
                            NotificationPublisher.queue("Combat", "you attacking to " + targets.get(index).getName(), NotificationType.WARNING);
                            canSendNotification = true;
                        }
                        final EntityLivingBase target = this.targets.get(this.index);
                        if (target != null) {
                            if (Spicy.INSTANCE.settingsManager.getSettingByName("auto-block").getValBoolean() && Wrapper.player().getCurrentEquippedItem() != null && Wrapper.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                Wrapper.playerController().sendUseItem(Wrapper.player(), Wrapper.mc().theWorld, Wrapper.player().getCurrentEquippedItem());
                                if (!noSlowdownModule.isModuleState() && Spicy.INSTANCE.settingsManager.getSettingByName("noslowdown").getValBoolean()) {
                                    Wrapper.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                }
                            }
                            final float[] rotations = RotationUtils.getRotations(target);
                            event.setYaw(rotations[0]);
                            event.setPitch(rotations[1]);
                            if (Spicy.INSTANCE.settingsManager.getSettingByName("head-rotations").getValBoolean()) {
                                KillAura.mc.thePlayer.rotationYawHead = event.getYaw();
                                KillAura.mc.thePlayer.renderYawOffset = event.getYaw();
                                KillAura.mc.thePlayer.rotationPitchHead = event.getPitch();
                            }
                        }
                        if (this.setupTick) {
                            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                            if (this.targets.size() > 0 && Spicy.INSTANCE.settingsManager.getSettingByName("criticals").getValBoolean() && Wrapper.player().isCollidedVertically) {
                                StateManager.setOffsetLastPacketAura(true);
                                event.setY(event.getY() + 0.07);
                                event.setOnGround(false);
                            }
                            if (this.timer.delay((float) Math.random())) {
                                this.incrementIndex();
                                this.switchingTargets = true;
                                this.timer.reset();
                            }
                        }
                        else {
                            if (this.targets.size() > 0 && Spicy.INSTANCE.settingsManager.getSettingByName("criticals").getValBoolean() && Wrapper.player().isCollidedVertically && this.bhopCheck()) {
                                event.setOnGround(false);
                            }
                            if (Wrapper.player().fallDistance > 0.0f && Wrapper.player().fallDistance < 0.66) {
                                event.setOnGround(true);
                            }
                        }
                    } else {
                        canSendNotification = false;
                    }
                    this.setupTick = !this.setupTick;
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPost(PlayerMotionUpdateEvent event) {
        if(event.getState() == PlayerMotionUpdateEvent.State.POST) {
            this.setModuleSuffix(Spicy.INSTANCE.settingsManager.getSettingByName("AuraMode").getValString());
            final String valString = Spicy.INSTANCE.settingsManager.getSettingByName("AuraMode").getValString();
            switch (valString) {
                case "Switch": {
                    if (!this.setupTick || this.targets.size() <= 0 || this.targets.get(this.index) == null || this.targets.size() <= 0) {
                        break;
                    }
                    final EntityLivingBase target = this.targets.get(this.index);
                    if (Wrapper.player().isBlocking()) {
                        Wrapper.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    attack(target);
                    if (Wrapper.player().isBlocking()) {
                        Wrapper.packet(new C08PacketPlayerBlockPlacement(Wrapper.player().inventory.getCurrentItem()));
                        break;
                    }
                    break;
                }
            }
        }
    }

    private boolean bhopCheck() {
        if (ModuleManager.getModule(Speed.class).isModuleState()) {
            if (Wrapper.player().moveForward != 0.0f || Wrapper.player().moveStrafing != 0.0f) {
                return false;
            }
        }
        return true;
    }


    private void incrementIndex() {
        ++this.index;
        if (this.index >= this.targets.size()) {
            this.index = 0;
        }
    }


    public List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : Wrapper.loadedEntityList()) {
            if (isEntityValid(entity)) {
                targets.add((EntityLivingBase)entity);
            }
        }
        targets.sort(new Comparator<EntityLivingBase>() {
            @Override
            public int compare(final EntityLivingBase target1, final EntityLivingBase target2) {
                return Math.round(target2.getHealth() - target1.getHealth());
            }
        });
        return targets;
    }


    public boolean isEntityValid(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (!Wrapper.player().isEntityAlive() || !entityLiving.isEntityAlive() || entityLiving.getDistanceToEntity(Wrapper.player()) > (Wrapper.player().canEntityBeSeen(entityLiving) ? Spicy.INSTANCE.settingsManager.getSettingByName("Reach").getValDouble() : 3.0)) {
                return false;
            }
            if (entityLiving.ticksExisted < this.ticksExisted) {
                return false;
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("players").getValBoolean() && entityLiving instanceof EntityPlayer) {
                final EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                return !FriendManager.isFriend(entityPlayer.getName());
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("entities").getValBoolean() && (entityLiving instanceof EntityMob || entityLiving instanceof EntityGhast || entityLiving instanceof EntityDragon || entityLiving instanceof EntityWither || entityLiving instanceof EntitySlime || (entityLiving instanceof EntityWolf && ((EntityWolf)entityLiving).getOwner() != Wrapper.player()))) {
                return true;
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("entities").getValBoolean() && entityLiving instanceof EntityGolem) {
                return true;
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("entities").getValBoolean() && (entityLiving instanceof EntityAnimal || entityLiving instanceof EntitySquid)) {
                return true;
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("entities").getValBoolean() && entityLiving instanceof EntityBat) {
                return true;
            }
            if (Spicy.INSTANCE.settingsManager.getSettingByName("entities").getValBoolean() && entityLiving instanceof EntityVillager) {
                return true;
            }
        }
        return false;
    }

    public void attack(final EntityLivingBase entity) {
        this.attack(entity, Spicy.INSTANCE.settingsManager.getSettingByName("criticals").getValBoolean());
    }

    public void attack(final EntityLivingBase entity, final boolean crit) {
        attacking = true;
        this.swingItem();
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(Wrapper.player().getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = Wrapper.player().fallDistance > 0.0f && !Wrapper.player().onGround && !Wrapper.player().isOnLadder() && !Wrapper.player().isInWater() && !Wrapper.player().isPotionActive(Potion.blindness) && Wrapper.player().ridingEntity == null;
        Wrapper.packet(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Wrapper.player().onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            Wrapper.player().onEnchantmentCritical(entity);
        }
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
        return "Auto hits entities";
    }

    private void swingItem() {
            Wrapper.player().swingItem();
    }





}
