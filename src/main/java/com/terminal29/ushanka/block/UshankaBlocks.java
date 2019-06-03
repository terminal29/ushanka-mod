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

    public static void init(){

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE), BLUE_STONE);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE), new BlockItem(BLUE_STONE, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_BRICK), BLUE_STONE_BRICK);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_BRICK), new BlockItem(BLUE_STONE_BRICK, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_SQUARE_BRICK), BLUE_STONE_SQUARE_BRICK);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_BLUE_STONE_SQUARE_BRICK), new BlockItem(BLUE_STONE_SQUARE_BRICK, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE), GRASSY_BLUE_STONE);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE), new BlockItem(GRASSY_BLUE_STONE, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_BRICK), GRASSY_BLUE_STONE_BRICK);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_BRICK), new BlockItem(GRASSY_BLUE_STONE_BRICK, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

        Registry.register(Registry.BLOCK, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_SQUARE_BRICK), GRASSY_BLUE_STONE_SQUARE_BRICK);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_BLUE_STONE_SQUARE_BRICK), new BlockItem(GRASSY_BLUE_STONE_SQUARE_BRICK, new Item.Settings().itemGroup(UshankaItems.CREATIVE_TAB)));

    }
}
