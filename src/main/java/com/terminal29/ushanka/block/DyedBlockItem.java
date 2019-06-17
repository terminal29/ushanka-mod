package com.terminal29.ushanka.block;

import com.terminal29.ushanka.ModInfo;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.util.DyeColor;

public class DyedBlockItem extends BlockItem {
    public DyedBlockItem(Block block_1, Settings item$Settings_1) {
        super(block_1, item$Settings_1);
    }

    public static DyeColor getColor(ItemStack stack) {
        try {
            return DyeColor.valueOf(stack.getTag().getString(ModInfo.PROPERTY_COLOR).toUpperCase());
        }catch(Exception e){
            return DyeColor.BLACK;
        }
    }


    @Override
    protected boolean place(ItemPlacementContext itemPlacementContext_1, BlockState blockState_1) {
        BlockState colouredState = blockState_1.with(UshankaBlocks.COLOR_PROPERTY, getColor(itemPlacementContext_1.getStack()));
        return super.place(itemPlacementContext_1, colouredState);
    }
}
