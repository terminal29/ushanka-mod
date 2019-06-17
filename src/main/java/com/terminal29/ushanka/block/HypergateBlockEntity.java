package com.terminal29.ushanka.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class HypergateBlockEntity extends BlockEntity implements Tickable {

    public HypergateBlockEntity() {
        super(UshankaBlocks.HYPERGATE_BLOCK_ENTITY_TYPE);
    }

    @Override
    public void tick() {

    }

    public static boolean shouldDrawSide(BlockState state, Direction direction){
        return state.get(Properties.FACING).equals(direction);
    }
}
