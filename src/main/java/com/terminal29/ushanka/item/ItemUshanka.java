package com.terminal29.ushanka.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class ItemUshanka extends ArmorItem {
    public ItemUshanka(Settings item$Settings_1) {
        super(new MaterialUshanka(), EquipmentSlot.HEAD, item$Settings_1);
    }

}
