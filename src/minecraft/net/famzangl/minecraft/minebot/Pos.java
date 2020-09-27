// 
// Decompiled by Procyon v0.5.36
// 

package net.famzangl.minecraft.minebot;

import net.famzangl.minecraft.minebot.ai.scripting.EntityPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;

public class Pos extends BlockPos
{
    public static BlockPos ZERO;
    
    public Pos(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public static BlockPos fromDir(final EnumFacing dir) {
        return Pos.ZERO.offset(dir);
    }
    
    public static BlockPos[] fromDir(final EnumFacing[] standable) {
        final BlockPos[] res = new BlockPos[standable.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = fromDir(standable[i]);
        }
        return res;
    }
    
    public static BlockPos minPos(final BlockPos p1, final BlockPos p2) {
        return new BlockPos(Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()), Math.min(p1.getZ(), p2.getZ()));
    }
    
    public static BlockPos maxPos(final BlockPos p1, final BlockPos p2) {
        return new BlockPos(Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()), Math.max(p1.getZ(), p2.getZ()));
    }
    
    public double distance(final Pos other) {
        return length(other.getX() - this.getX(), other.getY() - this.getY(), other.getZ() - this.getZ());
    }
    
    public static double length(final double dx, final double dy, final double dz) {
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public double distance(final EntityPos other) {
        return other.distance(this);
    }
    
    static {
        Pos.ZERO = new BlockPos(0, 0, 0);
    }
}
