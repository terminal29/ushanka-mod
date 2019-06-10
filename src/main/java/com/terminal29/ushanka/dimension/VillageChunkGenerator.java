package com.terminal29.ushanka.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class VillageChunkGenerator extends ChunkGenerator<VillageChunkGeneratorConfig> {

    public VillageChunkGenerator(IWorld iWorld_1, BiomeSource biomeSource_1, VillageChunkGeneratorConfig chunkGeneratorConfig_1) {
        super(iWorld_1, biomeSource_1, chunkGeneratorConfig_1);
        VillageIslandManager.INSTANCE.setWorld(iWorld_1);
    }

    @Override
    public void buildSurface(Chunk chunk) {
        if(VillageIslandManager.INSTANCE.isIslandChunk(chunk.getPos())){
            VillageIslandManager.INSTANCE.chunkToIsland(chunk.getPos()).buildInChunk(this.world, chunk);
        }
    }

    @Override
    public int getSpawnHeight() {
        return 0;
    }

    @Override
    public void populateNoise(IWorld iWorld, Chunk chunk) {

    }

    @Override
    public int getHeightOnGround(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
