package com.terminal29.ushanka.dimension.generator;

import com.mojang.datafixers.Dynamic;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class IslandFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    public IslandFeature() {
        super(DefaultFeatureConfig::deserialize);
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator_1, Random random_1, int int_1, int int_2) {
        return true;
    }

    @Override
    protected int getSeedModifier() {
        return 0;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return IslandFeature.Start::new;
    }

    @Override
    public String getName() {
        return ModInfo.ISLAND_FEATURE_NAME;
    }

    @Override
    public int getRadius() {
        return 0;
    }

    public static class Start extends StructureStart {
        public Start(StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1) {
            super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
        }

        public void initialize(ChunkGenerator<?> chunkGenerator_1, StructureManager structureManager_1, int int_1, int int_2, Biome biome_1) {
            DefaultFeatureConfig defaultFeatureConfig_1 = (DefaultFeatureConfig)chunkGenerator_1.getStructureConfig(biome_1, UshankaDimensions.VILLAGE_ISLAND_FEATURE);
            int int_3 = int_1 * 16;
            int int_4 = int_2 * 16;
            BlockPos blockPos_1 = new BlockPos(int_3, 90, int_4);
            BlockRotation blockRotation_1 = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
            VillageIslandFeatureGenerator.addPieces(structureManager_1, blockPos_1, blockRotation_1, this.children, this.random, defaultFeatureConfig_1);
            this.setBoundingBoxFromChildren();
        }
    }
}
