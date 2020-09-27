// 
// Decompiled by Procyon v0.5.36
// 

package net.famzangl.minecraft.minebot.build.blockbuild;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.famzangl.minecraft.minebot.ai.BlockItemFilter;
import net.famzangl.minecraft.minebot.build.block.WoodItemFilter;
import net.famzangl.minecraft.minebot.build.block.WoodType;
import net.minecraft.util.BlockPos;
import net.famzangl.minecraft.minebot.ai.path.world.BlockSet;

public class WoodBuildTask extends CubeBuildTask
{
    public static final BlockSet BLOCKS;
    
    public WoodBuildTask(final BlockPos forPosition, final WoodType woodType) {
        this(forPosition, new WoodItemFilter(woodType));
    }
    
    private WoodBuildTask(final BlockPos forPosition, final BlockItemFilter woodItemFilter) {
        super(forPosition, woodItemFilter);
    }
    
    @Override
    public BuildTask withPositionAndRotation(final BlockPos add, final int rotateSteps, final MirrorDirection mirror) {
        return new WoodBuildTask(add, this.blockFilter);
    }
    
    static {
        BLOCKS = new BlockSet(new Block[] { Blocks.planks });
    }
}
