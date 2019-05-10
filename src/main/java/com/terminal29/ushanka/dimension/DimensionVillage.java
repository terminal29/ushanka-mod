package com.terminal29.ushanka.dimension;

import com.mojang.datafixers.Dynamic;
import com.terminal29.ushanka.ModInfo;
import net.minecraft.block.BlockState;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.chunk.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.level.LevelGeneratorType;

public class DimensionVillage extends Dimension {

    public static final DimensionType VILLAGE = Registry.register(Registry.DIMENSION, 249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, new DimensionTypeVillage(249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, ModInfo.DIMENSION_NAME, DimensionVillage::new, true));


    public DimensionVillage(World world_1, DimensionType dimensionType_1) {
        super(world_1, dimensionType_1);
    }

    public static void initStatic() {
        //VILLAGE = Registry.register(Registry.DIMENSION, 249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, new DimensionTypeVillage(249, ModInfo.DISPLAY_NAME + ":" + ModInfo.DIMENSION_NAME, ModInfo.DIMENSION_NAME, DimensionVillage::new, true));
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        ChunkGeneratorType<FlatChunkGeneratorConfig, FlatChunkGenerator> chunkGeneratorType_1 = ChunkGeneratorType.FLAT;
        BiomeSourceType<FixedBiomeSourceConfig, FixedBiomeSource> biomeSourceType_1 = BiomeSourceType.FIXED;
        FlatChunkGeneratorConfig flatChunkGeneratorConfig_1 = FlatChunkGeneratorConfig.getDefaultConfig();
        FixedBiomeSourceConfig fixedBiomeSourceConfig_1 = (biomeSourceType_1.getConfig()).setBiome(flatChunkGeneratorConfig_1.getBiome());
        return chunkGeneratorType_1.create(this.world, biomeSourceType_1.applyConfig(fixedBiomeSourceConfig_1), flatChunkGeneratorConfig_1);
    }

    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean b) {
        return new BlockPos(0, 100, 0);
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int int_1, int int_2, boolean boolean_1) {
        return new BlockPos(0, 100, 0);
    }

    @Override
    public float getSkyAngle(long l, float v) {
        return 0;
    }

    @Override
    public boolean hasVisibleSky() {
        return true;
    }

    @Override
    public Vec3d getFogColor(float v, float v1) {
        return new Vec3d(0, 0, 1);
    }

    @Override
    public boolean canPlayersSleep() {
        return true;
    }

    @Override
    public boolean shouldRenderFog(int i, int i1) {
        return false;
    }

    @Override
    public DimensionType getType() {
        return VILLAGE;
    }
}
