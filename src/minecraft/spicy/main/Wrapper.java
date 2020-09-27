package spicy.main;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import spicy.events.player.PlayerMoveEvent;
import spicy.module.Module;
import spicy.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class Wrapper {


    public static EntityPlayerSP player() {
        return mc().thePlayer;
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static MovementInput movementInput() {
        return player().movementInput;
    }

    public static float yaw() {
        return mc().thePlayer.rotationYaw;
    }

    public static float pitch() {
        return mc().thePlayer.rotationPitch;
    }

    public static double x() {
        return player().posX;
    }

    public static void x(final double x) {
        player().posX = x;
    }

    public static double y() {
        return player().posY;
    }

    public static void y(final double y) {
        player().posY = y;
    }

    public static double z() {
        return player().posZ;
    }

    public static void z(final double z) {
        player().posZ = z;
    }

    public static void yaw(final float yaw) {
        player().rotationYaw = yaw;
    }

    public static void pitch(final float pitch) {
        player().rotationPitch = pitch;
    }

    public static void packet(final Packet packet) {
        mc().getNetHandler().addToSendQueue(packet);
    }

    public static void setMoveSpeed(final PlayerMoveEvent event, final double speed) {
        double forward = movementInput().moveForward;
        double strafe = movementInput().moveStrafe;
        float yaw = yaw();
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static boolean isOnLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0).contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlock(var11, var12, var13);
                    if (!(var14 instanceof BlockAir) && !(var14 instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isOnAir() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0).contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlock(var11, var12, var13);
                    if (!(var14 instanceof BlockAir)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isInAir() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlock(var11, var12, var13);
                    if (var14 instanceof BlockAir) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isInLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlock(var11, var12, var13);
                    if (var14 instanceof BlockLiquid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Wrapper.player().isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Wrapper.player().getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static List<Entity> loadedEntityList() {
        final List<Entity> loadedList = new ArrayList<Entity>(mc().theWorld.loadedEntityList);
        loadedList.remove(player());
        return loadedList;
    }

    public static PlayerControllerMP playerController() {
        return mc().playerController;
    }

    public static void addChatMessage(final String s) {
        if (mc().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "§8[§5S" + "§fpicy§8] " + EnumChatFormatting.WHITE + s));
        }
    }

    public static boolean onSendChatMessage(final String s) {
        if (s.startsWith("$")) {
            Spicy.INSTANCE.commandManager.callCommand(s.substring(1));
            return false;
        }
        for (final Module mod : ModuleManager.getModules()) {
            if (mod.isModuleState()) {
                return mod.onSendChatMessage(s);
            }
        }
        return true;
    }

    public static WorldClient world() {
        return mc().theWorld;
    }
}
