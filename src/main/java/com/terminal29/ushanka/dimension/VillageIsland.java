package com.terminal29.ushanka.dimension;

import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
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

    public boolean loadFromFile(IWorld world, Identifier identifier){
        if (world instanceof ServerWorld) {
            try {
                ServerWorld serverWorld = (ServerWorld) world;
                StructureManager structureManager = serverWorld.getStructureManager();
                Structure islandStructure = structureManager.getStructure(identifier);
                StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setMirrored(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true).setChunkPosition((ChunkPos) null);

                islandStructure.place(world, new BlockPos(baseChunk.getStartX(), 0, baseChunk.getStartZ()), structurePlacementData_1);
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
            BlockPos size = new BlockPos(16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH, 128, 16 * VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH);
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
            chunk.setBlockState(new BlockPos(0, 0, 0), Blocks.REDSTONE_BLOCK.getDefaultState(), false);
            chunk.setBlockState(new BlockPos(16, 0, 0), Blocks.REDSTONE_BLOCK.getDefaultState(), false);
            chunk.setBlockState(new BlockPos(0, 0, 16), Blocks.REDSTONE_BLOCK.getDefaultState(), false);
            chunk.setBlockState(new BlockPos(16, 0, 16), Blocks.REDSTONE_BLOCK.getDefaultState(), false);
        }
    }
}
