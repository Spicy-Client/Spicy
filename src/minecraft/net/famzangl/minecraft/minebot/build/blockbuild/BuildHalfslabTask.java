// 
// Decompiled by Procyon v0.5.36
// 

package net.famzangl.minecraft.minebot.build.blockbuild;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.famzangl.minecraft.minebot.ai.task.place.SneakAndPlaceAtHalfTask;
import net.minecraft.util.Vec3i;
import net.famzangl.minecraft.minebot.ai.ItemFilter;
import net.famzangl.minecraft.minebot.ai.task.place.JumpingPlaceAtHalfTask;
import net.famzangl.minecraft.minebot.ai.task.AITask;
import net.famzangl.minecraft.minebot.ai.BlockItemFilter;
import net.famzangl.minecraft.minebot.build.block.SlabFilter;
import net.minecraft.util.BlockPos;
import net.famzangl.minecraft.minebot.build.block.SlabType;
import net.famzangl.minecraft.minebot.ai.task.BlockHalf;
import net.famzangl.minecraft.minebot.ai.path.world.BlockSet;

public class BuildHalfslabTask extends CubeBuildTask
{
    public static final BlockSet BLOCKS;
    private final BlockHalf side;
    private final SlabType slabType;
    
    public BuildHalfslabTask(final BlockPos forPosition, final SlabType slabType, final BlockHalf up) {
        super(forPosition, new SlabFilter(slabType));
        this.slabType = slabType;
        this.side = up;
    }
    
    @Override
    public AITask getPlaceBlockTask(final BlockPos relativeFromPos) {
        if (!this.isStandablePlace(relativeFromPos)) {
            throw new IllegalArgumentException("Cannot build standing there: " + relativeFromPos);
        }
        if (relativeFromPos.equals((Object)BuildHalfslabTask.FROM_GROUND)) {
            return new JumpingPlaceAtHalfTask(this.forPosition.add(0, 1, 0), this.blockFilter, this.side);
        }
        return new SneakAndPlaceAtHalfTask(this.forPosition.add(0, 1, 0), this.blockFilter, this.forPosition.add((Vec3i)relativeFromPos), this.getMinHeightToBuild(), this.side);
    }
    
    @Override
    protected double getBlockHeight() {
        return (this.side == BlockHalf.LOWER_HALF) ? 0.5 : 1.0;
    }
    
    @Override
    public String toString() {
        return "BuildHalfslabTask [side=" + this.side + ", blockFilter=" + this.blockFilter + ", forPosition=" + this.forPosition + "]";
    }
    
    @Override
    public BuildTask withPositionAndRotation(final BlockPos add, final int rotateSteps, final MirrorDirection mirror) {
        return new BuildHalfslabTask(add, this.slabType, this.side);
    }
    
    static {
        BLOCKS = new BlockSet(new Block[] { (Block)Blocks.stone_slab, (Block)Blocks.wooden_slab });
    }
}
