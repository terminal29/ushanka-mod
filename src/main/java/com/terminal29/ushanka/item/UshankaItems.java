package com.terminal29.ushanka.item;

import com.terminal29.ushanka.ModInfo;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UshankaItems {
    public static ItemGroup CREATIVE_TAB = FabricItemGroupBuilder.create(
            new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Items.USHANKA))
            .icon(() -> new ItemStack(new ItemUshanka(new Item.Settings())))
            .build();

    private static final Item USHANKA = new ItemUshanka(new Item.Settings().itemGroup(CREATIVE_TAB).stackSize(1));

    public static void init(){
        Registry.register(Registry.ITEM, new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Items.USHANKA), USHANKA);
    }
}
