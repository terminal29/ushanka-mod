package com.terminal29.ushanka.dimension;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public class DimensionTypeVillage extends DimensionType {
    protected DimensionTypeVillage(int int_1, String string_1, String string_2, BiFunction<World, DimensionType, ? extends Dimension> biFunction_1, boolean boolean_1) {
        super(int_1, string_1, string_2, biFunction_1, boolean_1);
    }
}
