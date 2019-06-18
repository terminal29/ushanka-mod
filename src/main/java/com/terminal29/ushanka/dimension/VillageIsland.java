package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IServerWorldExtension;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;

public class VillageIsland {
    private ChunkPos baseChunk;
    private Pair<Integer, Integer> islandPos;
    private int id;
    private final Object buildLock = new Object();
    private boolean hasBuilt = false;

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

    public void safePlace(Structure structure, IWorld iWorld_1, BlockPos blockPos_1, StructurePlacementData structurePlacementData_1) {
        if(iWorld_1 instanceof ServerWorld){
            ((IServerWorldExtension)iWorld_1).addOnTickAction( b-> structure.place(iWorld_1, blockPos_1, structurePlacementData_1));
        }
    }

    public boolean loadFromFile(IWorld world, Identifier identifier){
        if (world instanceof ServerWorld) {
            try {
                ServerWorld serverWorld = (ServerWorld) world;
                StructureManager structureManager = serverWorld.getStructureManager();
                Structure islandStructure = structureManager.getStructure(identifier);
                StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setMirrored(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true).setChunkPosition((ChunkPos) null);
                BlockPos position = new BlockPos(baseChunk.getStartX(), 0, baseChunk.getStartZ());
                safePlace(islandStructure, world, position, structurePlacementData_1);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean saveToFile(IWorld world, Identifier identifier){
        if(world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            StructureManager structureManager = serverWorld.getStructureManager();
            Structure islandStructure = structureManager.getStructureOrBlank(identifier);
            StructurePlacementData data = new StructurePlacementData();
            data.setBoundingBox(new MutableIntBoundingBox(new Vec3i(0,0,0), new Vec3i(16,128,16)));

            BlockPos corner = new BlockPos(getBaseChunkPos().getStartX(), 0, getBaseChunkPos().getStartZ());
            BlockPos size = new BlockPos(16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - 1, 128, 16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - 1);
            islandStructure.method_15174(serverWorld, corner, size, true, Blocks.STRUCTURE_VOID);
            islandStructure.setAuthor("");
            return structureManager.saveStructure(identifier);
        }
        return false;
    }

    public void buildInChunk(IWorld world, Chunk chunk) {

        synchronized (buildLock) {
            if (hasBuilt)
                return;
            hasBuilt = true;
        }
        Identifier structureIdentifier = ModInfo.getStructureIdentifier(this.id);

        if (!this.loadFromFile(world, structureIdentifier)) {
            System.out.println("Failed to load island: " + structureIdentifier);
        }
    }

    public BoundingBox getVillageBounds(){
        BlockPos corner1 = new BlockPos(baseChunk.getStartX(), 0, baseChunk.getStartZ());
        BlockPos corner2 = new BlockPos(16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - 1, 128, 16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - 1);
        corner2 = corner2.add(corner1.getX(), corner1.getY(), corner1.getZ());
        return new BoundingBox(corner1, corner2);
    }

    public BlockPos getSpawnpoint(){
        return new BlockPos(10,30,10);
    }

    public Vec3d getSkyColor(){
        int color = java.awt.Color.HSBtoRGB((float)(baseChunk.x * 0.21235 + baseChunk.z * 0.3612), 0.5f, 1);
        float r = ((color >> 16) & 0xff) / 255.0f;
        float g = ((color >> 8) & 0xff) / 255.0f;
        float b = (color & 0xff) / 255.0f;
        return new Vec3d(r,g,b);
    }
}
