package com.terminal29.ushanka;

import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public interface ICameraExtension {
    public void updateIsometric(BlockView blockView_1, Entity entity_1, boolean boolean_1, boolean boolean_2, float float_1);
}
