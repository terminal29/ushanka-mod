package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.dimension.generator.VillageChunkGenerator;
import com.terminal29.ushanka.dimension.generator.VillageChunkGeneratorConfig;
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
        return VillageIslandManager.INSTANCE.getIsland(0,0).getSpawnpoint();
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int int_1, int int_2, boolean boolean_1) {
        return VillageIslandManager.INSTANCE.getIsland(0,0).getSpawnpoint();
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

        return getSkyColor(new BlockPos(v,10,v1));
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

    public static Vec3d getSkyColor(BlockPos pos){
        try {
            return VillageIslandManager.INSTANCE.chunkToIsland(new ChunkPos(pos)).getSkyColor();
        }catch(Exception e){
            return new Vec3d(1,1,1);
        }
    }
}
