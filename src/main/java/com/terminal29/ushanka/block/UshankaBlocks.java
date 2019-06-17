package com.terminal29.ushanka.block;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.item.UshankaItems;
import com.terminal29.ushanka.render.HypergateBlockEntityRenderer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UshankaBlocks {

    public static EnumProperty<DyeColor> COLOR_PROPERTY = EnumProperty.of(ModInfo.PROPERTY_COLOR, DyeColor.class);

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

    public static final Block GREY_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());
    public static final Block GRASSY_GREY_STONE = new CutoutBlock(FabricBlockSettings.of(Material.STONE).build());

    public static final Block PIPE = new DyedBlock(FabricBlockSettings.of(Material.GLASS).lightLevel(15).build());
    public static final BlockItem PIPE_ITEM = new DyedBlockItem(PIPE, new Item.Settings().group(UshankaItems.CREATIVE_TAB));

    public static final HypergateBlock HYPERGATE = new HypergateBlock(FabricBlockSettings.of(Material.STONE).lightLevel(8).breakByHand(false).build());
    public static final BlockEntityType<HypergateBlockEntity> HYPERGATE_BLOCK_ENTITY_TYPE;

    static void registerBlock(Block block, Identifier identifier){
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new Item.Settings().group(UshankaItems.CREATIVE_TAB)));
    }

    static void registerBlockAndItem(Block block, BlockItem item, Identifier identifier){
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, item);
    }

    static{

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

        registerBlock(GREY_STONE, ModInfo.identifierFor(ModInfo.BLOCK_GREY_STONE));
        registerBlock(GRASSY_GREY_STONE, ModInfo.identifierFor(ModInfo.BLOCK_GRASSY_GREY_STONE));

        registerBlockAndItem(PIPE, PIPE_ITEM, ModInfo.identifierFor(ModInfo.BLOCK_PIPE));

        HYPERGATE_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY, ModInfo.BLOCK_ENTITY_HYPERGATE, BlockEntityType.Builder.create(HypergateBlockEntity::new, HYPERGATE).build(null));
        registerBlock(HYPERGATE, ModInfo.identifierFor(ModInfo.BLOCK_HYPERGATE));
        BlockEntityRendererRegistry.INSTANCE.register(HypergateBlockEntity.class, new HypergateBlockEntityRenderer());

        ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> DyedBlock.getColor(block).getSignColor(), PIPE);

        ColorProviderRegistry.ITEM.register((itemStack, i) -> DyedBlockItem.getColor(itemStack).getSignColor(), PIPE_ITEM);
    }

    public static void init(){
    }
}
