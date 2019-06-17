package com.terminal29.ushanka.block;

import com.sun.istack.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class HypergateBlock extends BlockWithEntity{
    public HypergateBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        Direction direction_1 = itemPlacementContext_1.getPlayerFacing();
        BlockPos blockPos_1 = itemPlacementContext_1.getBlockPos();
        BlockPos blockPos_2 = blockPos_1.offset(direction_1);
        return itemPlacementContext_1.getWorld().getBlockState(blockPos_2).canReplace(itemPlacementContext_1) ? this.getDefaultState().with(Properties.FACING, direction_1.getOpposite()) : null;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        return getOutlineShape(blockState_1, blockView_1, blockPos_1, entityContext_1);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        //return super.getCollisionShape(blockState_1, blockView_1, blockPos_1, entityContext_1);
        switch(blockState_1.get(Properties.FACING)){
            case NORTH:
            case SOUTH:
                return Block.createCuboidShape(-16,0,0,32,16,16);
            case EAST:
            case WEST:
                return Block.createCuboidShape(0,0,-16,16,16,32);
            default:
                return super.getOutlineShape(blockState_1, blockView_1, blockPos_1, entityContext_1);

        }
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(Properties.FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new HypergateBlockEntity();
    }
}
