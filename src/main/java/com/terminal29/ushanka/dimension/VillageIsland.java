package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;

public class VillageIsland {
    private ChunkPos baseChunk;
    private Pair<Integer, Integer> islandPos;
    private int id;
    boolean hasBuilt = false;

    public VillageIsland(int id, ChunkPos baseChunk){
        this.id = id;
        islandPos = new Pair<>(
                (int)Math.floor(baseChunk.x/(VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING)),
                (int)Math.floor(baseChunk.z/(VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING))
        );
        this.baseChunk = new ChunkPos(
                baseChunk.x * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING),
                baseChunk.z * (VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH+VillageIslandManager.ISLAND_CHUNK_SPACING)
        );
    }

    public VillageIsland(int id, Pair<Integer, Integer> islandPos){
        this.id = id;
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

    public void buildInChunk(IWorld world, Chunk chunk){
        if(world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            StructureManager structureManager = serverWorld.getStructureManager();
            Identifier islandIdentifier = ModInfo.getStructureIdentifier(this.id);
            Structure islandStructure = structureManager.getStructure(islandIdentifier);
            if(islandStructure == null){
                System.out.println("Failed to load island: " + islandIdentifier);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        chunk.setBlockState(new BlockPos(x, 0, z), Blocks.REDSTONE_BLOCK.getDefaultState(), false);
                    }
                }
            }else{
                StructurePlacementData data = new StructurePlacementData();
                data.setBoundingBox(new MutableIntBoundingBox(new Vec3i(0,0,0), new Vec3i(16,128,16)));
                data.setChunkPosition(chunk.getPos());
                islandStructure.place(world, new BlockPos(0,0,0), data);
            }



        }
    }
}
