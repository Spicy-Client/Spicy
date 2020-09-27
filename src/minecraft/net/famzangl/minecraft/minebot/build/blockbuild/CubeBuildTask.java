// 
// Decompiled by Procyon v0.5.36
// 

package net.famzangl.minecraft.minebot.build.blockbuild;

import net.famzangl.minecraft.minebot.Pos;
import net.famzangl.minecraft.minebot.ai.path.world.BlockSets;
import net.famzangl.minecraft.minebot.ai.AIHelper;
import net.famzangl.minecraft.minebot.ai.task.place.SneakAndPlaceTask;
import net.minecraft.util.Vec3i;
import net.famzangl.minecraft.minebot.ai.ItemFilter;
import net.famzangl.minecraft.minebot.ai.task.move.UpwardsMoveTask;
import net.famzangl.minecraft.minebot.ai.task.AITask;
import net.minecraft.util.BlockPos;
import net.famzangl.minecraft.minebot.ai.BlockItemFilter;

public abstract class CubeBuildTask extends BuildTask
{
    protected final BlockItemFilter blockFilter;
    protected static final BlockPos FROM_GROUND;
    public static final BlockPos[] STANDABLE;
    
    protected CubeBuildTask(final BlockPos forPosition, final BlockItemFilter blockFilter) {
        super(forPosition);
        this.blockFilter = blockFilter;
    }
    
    @Override
    public AITask getPlaceBlockTask(final BlockPos relativeFromPos) {
        if (!this.isStandablePlace(relativeFromPos)) {
            return null;
        }
        if (relativeFromPos.equals((Object)CubeBuildTask.FROM_GROUND)) {
            return new UpwardsMoveTask(this.forPosition.add(0, 1, 0), this.blockFilter);
        }
        return new SneakAndPlaceTask(this.forPosition.add(0, 1, 0), this.blockFilter, this.forPosition.add((Vec3i)relativeFromPos), this.getMinHeightToBuild());
    }
    
    protected double getMinHeightToBuild() {
        return this.forPosition.getY() + this.getBlockHeight();
    }
    
    protected double getBlockHeight() {
        return 1.0;
    }
    
    @Override
    public BlockPos[] getStandablePlaces() {
        return CubeBuildTask.STANDABLE;
    }
    
    @Override
    public boolean couldBuildFrom(final AIHelper helper, final int x, final int y, final int z) {
        return super.couldBuildFrom(helper, x, y, z) && !BlockSets.AIR.isAt(helper.getWorld(), x, y - 1, z);
    }
    
    @Override
    public ItemFilter getRequiredItem() {
        return this.blockFilter;
    }
    
    static {
        FROM_GROUND = Pos.ZERO;
        STANDABLE = new BlockPos[] { new BlockPos(-1, 1, 0), new BlockPos(0, 1, -1), new BlockPos(1, 1, 0), new BlockPos(0, 1, 1), CubeBuildTask.FROM_GROUND };
    }
}
