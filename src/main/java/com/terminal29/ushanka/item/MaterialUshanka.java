package com.terminal29.ushanka.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MaterialUshanka implements ArmorMaterial {

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
        return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        return "ushanka";
    }

    @Override
    public float getToughness() {
        return 0;
    }
}