package com.terminal29.ushanka.extension;

import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public interface ICameraExtension {
    void updateIsometric(BlockView blockView_1, Entity entity_1, boolean boolean_1, boolean boolean_2, float float_1);
}
