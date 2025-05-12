package me.xoq.tide.events.world;

import me.xoq.tide.events.CancellableEvent;
import net.minecraft.block.BlockState;

public class BlockInteractEvent extends CancellableEvent {
    private final BlockState blockState;

    public BlockInteractEvent(BlockState blockState) {
        this.blockState  = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }
}
