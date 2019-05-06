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

    public static final DimensionType VILLAGE = Registry.register(Registry.DIMENSION, new Identifier(ModInfo.DISPLAY_NAME, ModInfo.DIMENSION_NAME), new DimensionTypeVillage(249, "", ModInfo.DIMENSION_NAME, DimensionVillage::new, true));

    public DimensionVillage(World world_1, DimensionType dimensionType_1) {
        super(world_1, dimensionType_1);
    }

    public static void initStatic() {
        // does nothing but VILLAGE will be initialized;
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        ChunkGeneratorType<FlatChunkGeneratorConfig, FlatChunkGenerator> chunkGeneratorType_1 = ChunkGeneratorType.FLAT;
        BiomeSourceType<FixedBiomeSourceConfig, FixedBiomeSource> biomeSourceType_1 = BiomeSourceType.FIXED;
        FlatChunkGeneratorConfig flatChunkGeneratorConfig_1 = FlatChunkGeneratorConfig.getDefaultConfig();
        FixedBiomeSourceConfig fixedBiomeSourceConfig_1 = (biomeSourceType_1.getConfig()).setBiome(flatChunkGeneratorConfig_1.getBiome());
        return chunkGeneratorType_1.create(this.world, biomeSourceType_1.applyConfig(fixedBiomeSourceConfig_1), flatChunkGeneratorConfig_1);}

    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean b) {
        for(int int_1 = chunkPos.getStartX(); int_1 <= chunkPos.getEndX(); ++int_1) {
            for(int int_2 = chunkPos.getStartZ(); int_2 <= chunkPos.getEndZ(); ++int_2) {
                BlockPos blockPos_1 = this.getTopSpawningBlockPosition(int_1, int_2, b);
                if (blockPos_1 != null) {
                    return blockPos_1;
                }
            }
        }
        return null;
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int int_1, int int_2, boolean boolean_1) {
        BlockPos.Mutable blockPos$Mutable_1 = new BlockPos.Mutable(int_1, 0, int_2);
        Biome biome_1 = this.world.getBiome(blockPos$Mutable_1);
        BlockState blockState_1 = biome_1.getSurfaceConfig().getTopMaterial();
        if (boolean_1 && !blockState_1.getBlock().matches(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            WorldChunk worldChunk_1 = this.world.method_8497(int_1 >> 4, int_2 >> 4);
            int int_3 = worldChunk_1.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, int_1 & 15, int_2 & 15);
            if (int_3 < 0) {
                return null;
            } else if (worldChunk_1.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, int_1 & 15, int_2 & 15) > worldChunk_1.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR, int_1 & 15, int_2 & 15)) {
                return null;
            } else {
                for(int int_4 = int_3 + 1; int_4 >= 0; --int_4) {
                    blockPos$Mutable_1.set(int_1, int_4, int_2);
                    BlockState blockState_2 = this.world.getBlockState(blockPos$Mutable_1);
                    if (!blockState_2.getFluidState().isEmpty()) {
                        break;
                    }

                    if (blockState_2.equals(blockState_1)) {
                        return blockPos$Mutable_1.up().toImmutable();
                    }
                }

                return null;
            }
        }
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
        return new Vec3d(0,0,1);
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
