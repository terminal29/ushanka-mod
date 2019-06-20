package com.terminal29.ushanka.item;

import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;

public class ItemUshanka extends ArmorItem {
    public ItemUshanka(Settings item$Settings_1) {
        super(new MaterialUshanka(), EquipmentSlot.HEAD, item$Settings_1);
    }
}
