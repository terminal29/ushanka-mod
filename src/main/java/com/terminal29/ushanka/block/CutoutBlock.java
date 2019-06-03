package com.terminal29.ushanka.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;

class CutoutBlock extends Block {

    public CutoutBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}