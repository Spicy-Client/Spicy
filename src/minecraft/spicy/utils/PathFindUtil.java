package spicy.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

import java.util.ArrayList;
import java.util.List;

public class PathFindUtil
{
    public EntityPlayer pos;
    public PathFinder pathFinder;
    private float yaw;
    private float pitch;
    public static float fakeYaw;
    public static float fakePitch;
    private BlockPos block;
    private List<BlockPos> blocks;

    public PathFindUtil(final String name) {
        this.pathFinder = new PathFinder(new WalkNodeProcessor());
        this.blocks = new ArrayList<BlockPos>();
        for (final Object i : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (i instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)i;
                if (!player.getName().contains(name)) {
                    continue;
                }
                this.pos = player;
            }
        }
        if (this.pos != null) {
            this.move();
            final float[] rot = this.getRotationTo(this.pos.getPositionVector());
            PathFindUtil.fakeYaw = rot[0];
            PathFindUtil.fakePitch = rot[1];
        }
    }

    public PathFindUtil(final Block bs) {
        this.pathFinder = new PathFinder(new WalkNodeProcessor());
        this.blocks = new ArrayList<BlockPos>();
        final int distance = 50;
        final int posX = Minecraft.getMinecraft().thePlayer.getPosition().getX();
        final int posY = Minecraft.getMinecraft().thePlayer.getPosition().getY();
        final int posZ = Minecraft.getMinecraft().thePlayer.getPosition().getZ();
        for (int x = posX - distance; x < posX + distance; ++x) {
            for (int y = posY - distance; y < posY + distance; ++y) {
                for (int z = posZ - distance; z < posZ + distance; ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock().equals(bs) && Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() != null && Minecraft.getMinecraft().theWorld.getBlockState(pos) != null && Minecraft.getMinecraft().thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 0.3) {
                        final PathEntity pe = this.pathFinder.createEntityPathTo(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, pos.getX(), pos.getY(), pos.getZ(), (float)distance);
                        if (pe != null && pe.getCurrentPathLength() > 1) {
                            final PathPoint point = pe.getPathPointFromIndex(1);
                            final float[] rot = this.getRotationTo(new Vec3(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5));
                            this.yaw = rot[0];
                            this.pitch = rot[1];
                            Minecraft.getMinecraft().thePlayer.rotationYaw = this.yaw;
                            if (Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                                Minecraft.getMinecraft().thePlayer.rotationPitch = this.pitch;
                            }
                            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
                            final EntityPlayerSP thePlayer2 = Minecraft.getMinecraft().thePlayer;
                            final double n = 0.0;
                            thePlayer2.motionZ = n;
                            thePlayer.motionX = n;
                            final double offset = 0.26;
                            final double newx = Math.sin(this.yaw * 3.1415927f / 180.0f) * offset;
                            final double newz = Math.cos(this.yaw * 3.1415927f / 180.0f) * offset;
                            final EntityPlayerSP thePlayer3 = Minecraft.getMinecraft().thePlayer;
                            thePlayer3.motionX -= newx;
                            final EntityPlayerSP thePlayer4 = Minecraft.getMinecraft().thePlayer;
                            thePlayer4.motionZ += newz;
                            if (Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                                Minecraft.getMinecraft().thePlayer.jump();
                            }
                            Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = (Minecraft.getMinecraft().objectMouseOver.hitVec != null);
                        }
                        else {
                            final float[] rot2 = this.getRotationTo(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                            this.yaw = rot2[0];
                            this.pitch = rot2[1];
                            Minecraft.getMinecraft().thePlayer.rotationYaw = this.yaw;
                            Minecraft.getMinecraft().thePlayer.rotationPitch = this.pitch;
                            Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = (Minecraft.getMinecraft().objectMouseOver.hitVec != null);
                            if (Minecraft.getMinecraft().thePlayer.onGround) {
                                Minecraft.getMinecraft().thePlayer.jump();
                            }
                        }
                    }
                }
            }
        }
    }

    public EnumFacing getMouseOverFacing() {
        if (Minecraft.getMinecraft().objectMouseOver != null) {
            if ((int)Minecraft.getMinecraft().thePlayer.posZ < (int)Minecraft.getMinecraft().objectMouseOver.hitVec.zCoord) {
                return EnumFacing.SOUTH;
            }
            if ((int)Minecraft.getMinecraft().thePlayer.posZ > (int)Minecraft.getMinecraft().objectMouseOver.hitVec.zCoord) {
                return EnumFacing.NORTH;
            }
            if ((int)Minecraft.getMinecraft().thePlayer.posX < (int)Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord) {
                return EnumFacing.EAST;
            }
            if ((int)Minecraft.getMinecraft().thePlayer.posX > (int)Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord) {
                return EnumFacing.WEST;
            }
            if ((int)Minecraft.getMinecraft().thePlayer.posY > (int)Minecraft.getMinecraft().objectMouseOver.hitVec.yCoord) {
                return EnumFacing.DOWN;
            }
            if ((int)Minecraft.getMinecraft().thePlayer.posY < (int)Minecraft.getMinecraft().objectMouseOver.hitVec.yCoord) {
                return EnumFacing.UP;
            }
        }
        return null;
    }

    public void move() {
        if (Minecraft.getMinecraft().thePlayer.getDistance(this.pos.posX + 0.5, this.pos.posY + 0.5, this.pos.posZ + 0.5) > 0.3) {
            final PathEntity pe = this.pathFinder.createEntityPathTo(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, this.pos, 100.0f);
            if (pe != null && pe.getCurrentPathLength() > 1) {
                final PathPoint point = pe.getPathPointFromIndex(1);
                final float[] rot = this.getRotationTo(new Vec3(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5));
                this.yaw = rot[0];
                final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
                final EntityPlayerSP thePlayer2 = Minecraft.getMinecraft().thePlayer;
                final double n = 0.0;
                thePlayer2.motionZ = n;
                thePlayer.motionX = n;
                final double offset = 0.26;
                final double newx = Math.sin(this.yaw * 3.1415927f / 180.0f) * offset;
                final double newz = Math.cos(this.yaw * 3.1415927f / 180.0f) * offset;
                final EntityPlayerSP thePlayer3 = Minecraft.getMinecraft().thePlayer;
                thePlayer3.motionX -= newx;
                final EntityPlayerSP thePlayer4 = Minecraft.getMinecraft().thePlayer;
                thePlayer4.motionZ += newz;
                if (Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                    Minecraft.getMinecraft().thePlayer.jump();
                }
            }
        }
    }

    public float[] getRotationTo(final Vec3 pos) {
        final double xD = Minecraft.getMinecraft().thePlayer.posX - pos.xCoord;
        final double yD = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - pos.yCoord;
        final double zD = Minecraft.getMinecraft().thePlayer.posZ - pos.zCoord;
        final double yaw = Math.atan2(zD, xD);
        final double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[] { (float)Math.toDegrees(yaw) + 90.0f, (float)Math.toDegrees(pitch) };
    }
}
