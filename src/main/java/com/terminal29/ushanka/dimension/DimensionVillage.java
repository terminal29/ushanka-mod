package com.terminal29.ushanka.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;

public class DimensionVillage extends Dimension {
    public DimensionVillage(World world_1, DimensionType dimensionType_1) {
        super(world_1, dimensionType_1);
    }



    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        FixedBiomeSourceConfig cfg = new FixedBiomeSourceConfig();
        cfg.setBiome(UshankaDimensions.VILLAGE_BIOME);
        return new VillageChunkGenerator(this.world, new FixedBiomeSource(cfg), new VillageChunkGeneratorConfig());
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
        return new Vec3d(135/255.0,206/255.0,235/255.0);
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
        return UshankaDimensions.VILLAGE;
    }

    public Vec3d getSkyColor(){
        return new Vec3d(94/255.0,157/255.0,52/255.0);
    }
}
