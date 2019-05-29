package com.terminal29.ushanka.dimension;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.MathUtilities;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillageIslandManager {
    public static final VillageIslandManager INSTANCE = new VillageIslandManager();
    public static final int ISLAND_MAX_CHUNK_WIDTH = 4;
    public static final int ISLAND_CHUNK_SPACING = 8;

    private IWorld world;
    private Map<Pair<Integer, Integer>, VillageIsland> islands = new HashMap<>();

    public VillageIslandManager(){

    }



    public boolean isIslandChunk(ChunkPos pos){
        return (MathUtilities.mod(pos.x, (ISLAND_MAX_CHUNK_WIDTH + ISLAND_CHUNK_SPACING)) < ISLAND_MAX_CHUNK_WIDTH)
                && (MathUtilities.mod(pos.z, (ISLAND_MAX_CHUNK_WIDTH + ISLAND_CHUNK_SPACING)) < ISLAND_MAX_CHUNK_WIDTH);
    }

    public ChunkPos islandToChunk(VillageIsland island){
        return island.getBaseChunkPos();
    }

    public VillageIsland chunkToIsland(ChunkPos pos){
        if(!isIslandChunk(pos))
            return null;
        Pair<Integer, Integer> islandKey = new Pair<>(
                (int)Math.floor(pos.x / (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING)),
                (int)Math.floor(pos.z / (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING))
        );
        if(!islands.containsKey(islandKey)){
            islands.put(islandKey, new VillageIsland(pos));
        }
        return islands.get(islandKey);
    }


    public void setWorld(IWorld world){
        this.world = world;
        // new world, new islands
        islands = new HashMap<>();
    }
}
