package com.terminal29.ushanka.block;

import com.terminal29.ushanka.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class DyedBlock extends CutoutBlock{

    private static final EnumProperty<DyeColor> COLOR;

    public DyedBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(COLOR);
    }

    public static DyeColor getColor(BlockState state) {
        try {
            return state.get(UshankaBlocks.COLOR_PROPERTY);
        }catch(IllegalArgumentException e){
            return DyeColor.BLACK;
        }
    }

    @Override
    public ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1) {
        ItemStack thisStack = new ItemStack(this);
        thisStack.getOrCreateTag().putString(ModInfo.PROPERTY_COLOR, blockState_1.get(UshankaBlocks.COLOR_PROPERTY).getName());
        return thisStack;
    }

    static{
        COLOR = UshankaBlocks.COLOR_PROPERTY;
    }

}
