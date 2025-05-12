package me.xoq.tide.events.world;

import me.xoq.tide.events.CancellableEvent;
import net.minecraft.util.math.BlockPos;

public class BlockBreakEvent extends CancellableEvent {
    private final BlockPos blockPos;

    public BlockBreakEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
