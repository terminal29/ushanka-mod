package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceImpl;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IWorld;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VillageIslandManager {
    public static final VillageIslandManager INSTANCE = new VillageIslandManager();
    public static final int ISLAND_MAX_CHUNK_WIDTH = 4;
    public static final int ISLAND_CHUNK_SPACING = 8;

    private IWorld world;
    private Map<Pair<Integer, Integer>, VillageIsland> islands = new HashMap<>();

    private static Map<String, Resource> islandStructures = new HashMap<>();

    static{
        getOrLoadStructure(ModInfo.ISLAND_STRUCTURE_0 + ".nbt");
    }

    public static Resource getOrLoadStructure(String structureName){
        if(!islandStructures.containsKey(structureName)){
            Resource r = new StructureResource("ushanka", ModInfo.identifierFor(structureName), VillageIslandManager.class.getClassLoader().getResourceAsStream("assets/ushanka/structures/" + structureName), null);
            islandStructures.put(structureName, r);
        }
        return islandStructures.get(structureName);
    }


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
                Math.round((float)Math.floor((float)pos.x / (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING))),
                Math.round((float)Math.floor((float)pos.z / (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING)))
        );
        if(!islands.containsKey(islandKey)){
            islands.put(islandKey, new VillageIsland(0, islandKey));
        }
        return islands.get(islandKey);
    }


    public void setWorld(IWorld world){
        this.world = world;
        // new world, new islands
        islands = new HashMap<>();
    }
}
