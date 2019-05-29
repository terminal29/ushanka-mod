package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.ModInfo;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class UshankaDimensions {
    public static final DimensionType VILLAGE = Registry.register(Registry.DIMENSION, 249, ModInfo.identifierFor(ModInfo.Dimensions.DIMENSION_NAME).toString(), new DimensionTypeVillage(249, ModInfo.identifierFor(ModInfo.Dimensions.DIMENSION_NAME).toString(), ModInfo.Dimensions.DIMENSION_NAME, DimensionVillage::new, true));
    public static final Biome VILLAGE_BIOME = Registry.register(Registry.BIOME, ModInfo.identifierFor(ModInfo.Dimensions.BIOME), VillageBiome.Build());
    public static void init() {
    }
}
