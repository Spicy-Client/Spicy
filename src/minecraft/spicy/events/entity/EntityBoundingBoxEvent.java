package spicy.events.entity;

import com.darkmagician6.eventapi.events.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class EntityBoundingBoxEvent implements Event
{
    private Block block;
    private BlockPos blockPos;
    private AxisAlignedBB boundingBox;

    public EntityBoundingBoxEvent(final Block block, final BlockPos pos, final AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }
}
