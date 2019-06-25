package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.generator.IslandFeature;
import com.terminal29.ushanka.dimension.generator.VillageIslandFeatureGenerator;
import net.minecraft.resource.Resource;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class UshankaDimensions {
    public static final DimensionType VILLAGE = Registry.register(Registry.DIMENSION, 249, ModInfo.identifierFor(ModInfo.DIMENSION_NAME).toString(), new DimensionTypeVillage(249, ModInfo.identifierFor(ModInfo.DIMENSION_NAME).toString(), ModInfo.DIMENSION_NAME, DimensionVillage::new, true));
    public static final Biome VILLAGE_BIOME = Registry.register(Registry.BIOME, ModInfo.identifierFor(ModInfo.DIMENSION_BIOME), VillageBiome.Build());

    public static final StructurePieceType VILLAGE_ISLAND_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, ModInfo.identifierFor(ModInfo.ISLAND_STRUCTURE_PIECE), VillageIslandFeatureGenerator.Piece::new);
    public static final StructureFeature<DefaultFeatureConfig> VILLAGE_ISLAND_FEATURE = Registry.register(Registry.FEATURE, ModInfo.identifierFor(ModInfo.ISLAND_FEATURE_NAME), new IslandFeature());
    public static final StructureFeature<?> VILLAGE_ISLAND_STRUCTURE_PIECE = Registry.register(Registry.STRUCTURE_FEATURE, ModInfo.identifierFor(ModInfo.ISLAND_STRUCTURE_0), VILLAGE_ISLAND_FEATURE);


    public static void init() {
        Feature.STRUCTURES.put("island", VILLAGE_ISLAND_FEATURE);

        VILLAGE_BIOME.addStructureFeature(VILLAGE_ISLAND_FEATURE, new DefaultFeatureConfig());
        VILLAGE_BIOME.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Biome.configureFeature(VILLAGE_ISLAND_FEATURE, new DefaultFeatureConfig(), Decorator.CHANCE_PASSTHROUGH, new ChanceDecoratorConfig(0)));
    }
}
