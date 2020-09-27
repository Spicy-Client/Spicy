package spicy.module.modules.combat;


import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.friend.FriendManager;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.utils.RotationUtils;

public class Aimbot extends Module
{
    private double speed;
    public double degrees;
    private double range;
    private EntityLivingBase target;
    
    public Aimbot() {
        super("Aimbot", 0, Category.COMBAT);
        this.range = Spicy.INSTANCE.settingsManager.getSettingByName("reach").getValDouble();
        this.speed = 1.0;
        this.degrees = 9999999.0;
    }
    
    @Override
    public void onEnabled() {
        this.target = null;
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @SubscribeEvent(3)
    public void onUpdate(final PlayerMotionUpdateEvent local_01) {
        if (local_01.getState().equals(PlayerMotionUpdateEvent.State.PRE) && Wrapper.player().isEntityAlive()) {
            for (final Object local_2 : Wrapper.mc().theWorld.loadedEntityList) {
                if (local_2 instanceof EntityLivingBase) {
                    final Object local_3 = local_2;
                    if (this.isEntityValid((Entity)local_3) && ((EntityLivingBase)local_3).isEntityAlive()) {
                        this.target = (EntityLivingBase)local_3;
                    }
                    else {
                        this.target = null;
                    }
                    if (this.target == null) {
                        continue;
                    }
                    final EntityPlayerSP player4;
                    final EntityPlayerSP player3;
                    final EntityPlayerSP entityPlayerSP3;
                    final EntityPlayerSP entityPlayerSP = entityPlayerSP3 = (player3 = (player4 = Wrapper.player()));
                    entityPlayerSP3.rotationPitch += (float)(this.getPitchChange(this.target) / this.speed);
                    final EntityPlayerSP player6;
                    final EntityPlayerSP player5;
                    final EntityPlayerSP entityPlayerSP4;
                    final EntityPlayerSP entityPlayerSP2 = entityPlayerSP4 = (player5 = (player6 = Wrapper.player()));
                    entityPlayerSP4.rotationYaw += (float)(this.getYawChange(this.target) / this.speed);
                }
            }
        }
    }
    
    public float getPitchChange(final Entity local_01) {
        final double posX = local_01.posX;
        Wrapper.mc();
        final double local_2 = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double posZ = local_01.posZ;
        Wrapper.mc();
        final double local_3 = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double n = local_01.posY - 2.2 + local_01.getEyeHeight();
        Wrapper.mc();
        final double local_4 = n - Minecraft.getMinecraft().thePlayer.posY;
        final double local_5 = MathHelper.sqrt_double(local_2 * local_2 + local_3 * local_3);
        final double local_6 = -Math.toDegrees(Math.atan(local_4 / local_5));
        Wrapper.mc();
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)local_6) - 2.5f;
    }
    
    public float getYawChange(final Entity local_01) {
        final double posX = local_01.posX;
        Wrapper.mc();
        final double local_2 = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double posZ = local_01.posZ;
        Wrapper.mc();
        final double local_3 = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double local_4 = 0.0;
        if (local_3 < 0.0 && local_2 < 0.0) {
            local_4 = 90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else if (local_3 < 0.0 && local_2 > 0.0) {
            local_4 = -90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else {
            local_4 = Math.toDegrees(-Math.atan(local_2 / local_3));
        }
        Wrapper.mc();
        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float)local_4));
    }
    
    public boolean isEntityValid(final Entity local_01) {
        if (local_01 instanceof EntityLivingBase) {
            final Object local_2 = local_01;
            if (!Wrapper.player().isEntityAlive() || !((EntityLivingBase)local_2).isEntityAlive() || ((Entity)local_2).getDistanceToEntity(Wrapper.player()) > (Wrapper.player().canEntityBeSeen((Entity)local_2) ? this.range : 3.0)) {
                return false;
            }
            final double local_3 = local_01.posX - Wrapper.player().posX;
            final double local_4 = local_01.posZ - Wrapper.player().posZ;
            final double local_5 = Wrapper.player().posY + Wrapper.player().getEyeHeight() - (local_01.posY + local_01.getEyeHeight());
            final double local_6 = Math.sqrt(local_3 * local_3 + local_4 * local_4);
            final float local_7 = (float)(Math.atan2(local_4, local_3) * 180.0 / 3.141592653589793) - 90.0f;
            final float local_8 = (float)(Math.atan2(local_5, local_6) * 180.0 / 3.141592653589793);
            final double local_9 = RotationUtils.getDistanceBetweenAngles(local_7, Wrapper.player().rotationYaw % 360.0f);
            final double local_10 = RotationUtils.getDistanceBetweenAngles(local_8, Wrapper.player().rotationPitch % 360.0f);
            final double local_11 = Math.sqrt(local_9 * local_9 + local_10 * local_10);
            if (local_11 > this.degrees) {
                return false;
            }
            if (local_2 instanceof EntityPlayer) {
                final Object local_12 = local_2;
                return !FriendManager.isFriend(((EntityPlayer)local_12).getName());
            }
        }
        return false;
    }


    @Override
    public String getModuleDesc() {
        return "Auto aim players";
    }
}
