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


    public static final int ISLAND_MAX_CHUNK_WIDTH = 4;
    public static final int ISLAND_CHUNK_SPACING = 8;

    public VillageChunkGenerator(IWorld iWorld_1, BiomeSource biomeSource_1, VillageChunkGeneratorConfig chunkGeneratorConfig_1) {
        super(iWorld_1, biomeSource_1, chunkGeneratorConfig_1);
    }

    @Override
    public void buildSurface(Chunk chunk) {
        boolean doGenerateInChunk = shouldGenerateInChunk(chunk.getPos());
        if(doGenerateInChunk){
            for(int x = 0; x < 16; x++){
                for(int z = 0; z < 16; z++){
                    chunk.setBlockState(new BlockPos(x,0,z), Blocks.COAL_BLOCK.getDefaultState(), false);
                }
            }
        }
    }
    
    private static int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    private static boolean shouldGenerateInChunk(ChunkPos pos){
        return (mod(pos.x,(ISLAND_MAX_CHUNK_WIDTH + ISLAND_CHUNK_SPACING)) < ISLAND_MAX_CHUNK_WIDTH)
                && (mod(pos.z,(ISLAND_MAX_CHUNK_WIDTH + ISLAND_CHUNK_SPACING)) < ISLAND_MAX_CHUNK_WIDTH);
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
