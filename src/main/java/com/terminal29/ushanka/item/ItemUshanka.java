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
        super(new ArmorMaterial() {


            @Override
            public int getDurability(EquipmentSlot equipmentSlot) {
                return 0;
            }

            @Override
            public int getProtectionAmount(EquipmentSlot equipmentSlot) {
                return 0;
            }

            @Override
            public int getEnchantability() {
                return 0;
            }

            @Override
            public SoundEvent getEquipSound() {
                return null;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public float getToughness() {
                return 0;
            }
        }, EquipmentSlot.HEAD, item$Settings_1);
    }

}
