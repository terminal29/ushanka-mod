package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.MathUtilities;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class VillageIsland {
    private ChunkPos baseChunk;
    private Pair<Integer, Integer> islandPos;

    public VillageIsland(ChunkPos baseChunk){
        islandPos = new Pair<>(
                (int)Math.floor(baseChunk.x/(VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING)),
                (int)Math.floor(baseChunk.z/(VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING))
        );
        this.baseChunk = new ChunkPos(
                baseChunk.x * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING),
                baseChunk.z * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING)
        );
    }

    public VillageIsland(Pair<Integer, Integer> islandPos){
        this.islandPos = islandPos;
        this.baseChunk = new ChunkPos(
                islandPos.getLeft() * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING),
                islandPos.getRight() * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING)
        );
    }

    public ChunkPos getBaseChunkPos(){
        return baseChunk;
    }

    public Pair<Integer, Integer> getIslandPos(){
        return islandPos;
    }

    public void buildInChunk(Chunk chunk){
        // Replace this
        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++){
                chunk.setBlockState(new BlockPos(x,0,z), Blocks.COAL_BLOCK.getDefaultState(), false);
            }
        }
    }
}
