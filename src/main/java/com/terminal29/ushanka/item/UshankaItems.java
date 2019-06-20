package com.terminal29.ushanka.item;

import com.terminal29.ushanka.ModInfo;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.terminal29.ushanka.ModInfo.ITEM_TAB_ICON;
import static com.terminal29.ushanka.ModInfo.ITEM_USHANKA;

public class UshankaItems {
    private static final Item TAB_ICON = new Item(new Item.Settings());

    public static ItemGroup CREATIVE_TAB = FabricItemGroupBuilder
            .build(ModInfo.identifierFor(ModInfo.DISPLAY_NAME), () -> new ItemStack(TAB_ICON));

    private static final Item USHANKA = new ItemUshanka(new Item.Settings().group(CREATIVE_TAB).maxCount(1));

    public static void init(){
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ITEM_TAB_ICON), TAB_ICON);
        Registry.register(Registry.ITEM, ModInfo.identifierFor(ITEM_USHANKA), USHANKA);
    }
}
