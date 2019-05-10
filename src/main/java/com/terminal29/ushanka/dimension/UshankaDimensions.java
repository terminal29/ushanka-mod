package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.ModInfo;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class UshankaDimensions {
    public static final DimensionType VILLAGE = Registry.register(Registry.DIMENSION, 249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, new DimensionTypeVillage(249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, ModInfo.DIMENSION_NAME, DimensionVillage::new, true));
    public static void init() {
    }
}
