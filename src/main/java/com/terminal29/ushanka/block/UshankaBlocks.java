package com.terminal29.ushanka.block;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.item.UshankaItems;
import net.minecraft.block.Block;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UshankaBlocks {

    public static final Block BLUE_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block BLUE_STONE_BRICK = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block BLUE_STONE_SQUARE_BRICK = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());

    public static final Block GRASSY_BLUE_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block GRASSY_BLUE_STONE_BRICK = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block GRASSY_BLUE_STONE_SQUARE_BRICK = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());

    public static final Block BROWN_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block BROWN_STONE_INNER_SQUARE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block BROWN_STONE_OUTER_SQUARE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());

    public static final Block GRASSY_BROWN_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block GRASSY_BROWN_STONE_INNER_SQUARE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block GRASSY_BROWN_STONE_OUTER_SQUARE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());

    static void registerBlock(Block block, Identifier identifier){
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new Item.Settings().group(UshankaItems.CREATIVE_TAB)));
    }

    public static void init(){
        registerBlock(BLUE_STONE, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE));
        registerBlock(BLUE_STONE_BRICK, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_BRICK));
        registerBlock(BLUE_STONE_SQUARE_BRICK, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_SQUARE_BRICK));

        registerBlock(GRASSY_BLUE_STONE, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE));
        registerBlock(GRASSY_BLUE_STONE_BRICK, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_BRICK));
        registerBlock(GRASSY_BLUE_STONE_SQUARE_BRICK, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_SQUARE_BRICK));

        registerBlock(BROWN_STONE, ModInfo.identifierFor(ModInfo.BLOCK_BROWN_STONE));
        registerBlock(BROWN_STONE_INNER_SQUARE, ModInfo.identifierFor(ModInfo.BLOCK_BROWN_STONE_INNER_SQUARE));
        registerBlock(BROWN_STONE_OUTER_SQUARE, ModInfo.identifierFor(ModInfo.BLOCK_BROWN_STONE_OUTER_SQUARE));

        registerBlock(GRASSY_BROWN_STONE, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BROWN_STONE));
        registerBlock(GRASSY_BROWN_STONE_INNER_SQUARE, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BROWN_STONE_INNER_SQUARE));
        registerBlock(GRASSY_BROWN_STONE_OUTER_SQUARE, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BROWN_STONE_OUTER_SQUARE));
    }
}
