package com.terminal29.ushanka.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TickableItem {
    void tick(ItemStack itemStack, Entity entity, World world, InventoryType type);
}
