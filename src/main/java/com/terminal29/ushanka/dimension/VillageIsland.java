package com.terminal29.ushanka.dimension;

import com.google.common.collect.Lists;
import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IServerWorldExtension;
import com.terminal29.ushanka.extension.IStructureExtension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;
import java.util.List;

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

    public boolean safePlace(IStructureExtension structure, IWorld iWorld_1, BlockPos blockPos_1, StructurePlacementData structurePlacementData_1) {
        if(iWorld_1 instanceof ServerWorld){
            ((IServerWorldExtension)iWorld_1).addOnTickAction( b-> {
                int int_1 = 2;
                if (structure.getBlocks().isEmpty()) {
                    return;
                    //return false;
                } else {
                    List<Structure.StructureBlockInfo> list_1 = structurePlacementData_1.method_15121(structure.getBlocks(), blockPos_1);
                    if ((!list_1.isEmpty() || !structurePlacementData_1.shouldIgnoreEntities() && !structure.getEntities().isEmpty()) && structure.getSize().getX() >= 1 && structure.getSize().getY() >= 1 && structure.getSize().getZ() >= 1) {
                        MutableIntBoundingBox mutableIntBoundingBox_1 = structurePlacementData_1.method_15124();
                        List<BlockPos> list_2 = Lists.newArrayListWithCapacity(structurePlacementData_1.shouldPlaceFluids() ? list_1.size() : 0);
                        List<com.mojang.datafixers.util.Pair<BlockPos, CompoundTag>> list_3 = Lists.newArrayListWithCapacity(list_1.size());
                        int int_2 = 2147483647;
                        int int_3 = 2147483647;
                        int int_4 = 2147483647;
                        int int_5 = -2147483648;
                        int int_6 = -2147483648;
                        int int_7 = -2147483648;
                        List<Structure.StructureBlockInfo> list_4 = Structure.process(iWorld_1, blockPos_1, structurePlacementData_1, list_1);
                        Iterator var16 = list_4.iterator();

                        while(true) {
                            Structure.StructureBlockInfo structure$StructureBlockInfo_1;
                            BlockPos blockPos_2;
                            BlockEntity blockEntity_3;
                            do {
                                if (!var16.hasNext()) {
                                    boolean boolean_1 = true;
                                    Direction[] directions_1 = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

                                    Iterator iterator_1;
                                    BlockPos blockPos_7;
                                    BlockState blockState_4;
                                    while(boolean_1 && !list_2.isEmpty()) {
                                        boolean_1 = false;
                                        iterator_1 = list_2.iterator();

                                        while(iterator_1.hasNext()) {
                                            BlockPos blockPos_3 = (BlockPos)iterator_1.next();
                                            blockPos_7 = blockPos_3;
                                            FluidState fluidState_2 = iWorld_1.getFluidState(blockPos_3);

                                            for(int int_8 = 0; int_8 < directions_1.length && !fluidState_2.isStill(); ++int_8) {
                                                BlockPos blockPos_5 = blockPos_7.offset(directions_1[int_8]);
                                                FluidState fluidState_3 = iWorld_1.getFluidState(blockPos_5);
                                                if (fluidState_3.getHeight(iWorld_1, blockPos_5) > fluidState_2.getHeight(iWorld_1, blockPos_7) || fluidState_3.isStill() && !fluidState_2.isStill()) {
                                                    fluidState_2 = fluidState_3;
                                                    blockPos_7 = blockPos_5;
                                                }
                                            }

                                            if (fluidState_2.isStill()) {
                                                blockState_4 = iWorld_1.getBlockState(blockPos_3);
                                                Block block_1 = blockState_4.getBlock();
                                                if (block_1 instanceof FluidFillable) {
                                                    ((FluidFillable)block_1).tryFillWithFluid(iWorld_1, blockPos_3, blockState_4, fluidState_2);
                                                    boolean_1 = true;
                                                    iterator_1.remove();
                                                }
                                            }
                                        }
                                    }

                                    if (int_2 <= int_5) {
                                        if (!structurePlacementData_1.method_16444()) {
                                            VoxelSet voxelSet_1 = new BitSetVoxelSet(int_5 - int_2 + 1, int_6 - int_3 + 1, int_7 - int_4 + 1);
                                            int int_9 = int_2;
                                            int int_10 = int_3;
                                            int int_11 = int_4;
                                            Iterator var38 = list_3.iterator();

                                            while(var38.hasNext()) {
                                                com.mojang.datafixers.util.Pair<BlockPos, CompoundTag> pair_1 = (com.mojang.datafixers.util.Pair)var38.next();
                                                BlockPos blockPos_6 = (BlockPos)pair_1.getFirst();
                                                voxelSet_1.set(blockPos_6.getX() - int_9, blockPos_6.getY() - int_10, blockPos_6.getZ() - int_11, true, true);
                                            }

                                            Structure.method_20532(iWorld_1, int_1, voxelSet_1, int_9, int_10, int_11);
                                        }

                                        iterator_1 = list_3.iterator();

                                        while(iterator_1.hasNext()) {
                                            com.mojang.datafixers.util.Pair<BlockPos, CompoundTag> pair_2 = (com.mojang.datafixers.util.Pair)iterator_1.next();
                                            blockPos_7 = (BlockPos)pair_2.getFirst();
                                            if (!structurePlacementData_1.method_16444()) {
                                                BlockState blockState_3 = iWorld_1.getBlockState(blockPos_7);
                                                blockState_4 = Block.getRenderingState(blockState_3, iWorld_1, blockPos_7);
                                                if (blockState_3 != blockState_4) {
                                                    iWorld_1.setBlockState(blockPos_7, blockState_4, int_1 & -2 | 16);
                                                }

                                                iWorld_1.updateNeighbors(blockPos_7, blockState_4.getBlock());
                                            }

                                            if (pair_2.getSecond() != null) {
                                                blockEntity_3 = iWorld_1.getBlockEntity(blockPos_7);
                                                if (blockEntity_3 != null) {
                                                    blockEntity_3.markDirty();
                                                }
                                            }
                                        }
                                    }

                                    if (!structurePlacementData_1.shouldIgnoreEntities()) {
                                        structure.method_15179_accessor(iWorld_1, blockPos_1, structurePlacementData_1.getMirror(), structurePlacementData_1.getRotation(), structurePlacementData_1.getPosition(), mutableIntBoundingBox_1);
                                    }
                                    return;
                                    //return true;
                                }

                                structure$StructureBlockInfo_1 = (Structure.StructureBlockInfo)var16.next();
                                blockPos_2 = structure$StructureBlockInfo_1.pos;
                            } while(mutableIntBoundingBox_1 != null && !mutableIntBoundingBox_1.contains(blockPos_2));

                            FluidState fluidState_1 = structurePlacementData_1.shouldPlaceFluids() ? iWorld_1.getFluidState(blockPos_2) : null;
                            BlockState blockState_1 = structure$StructureBlockInfo_1.state.mirror(structurePlacementData_1.getMirror()).rotate(structurePlacementData_1.getRotation());
                            if (structure$StructureBlockInfo_1.tag != null) {
                                blockEntity_3 = iWorld_1.getBlockEntity(blockPos_2);
                                Clearable.clear(blockEntity_3);
                                iWorld_1.setBlockState(blockPos_2, Blocks.BARRIER.getDefaultState(), 4);
                            }

                            if (iWorld_1.setBlockState(blockPos_2, blockState_1, int_1)) {
                                int_2 = Math.min(int_2, blockPos_2.getX());
                                int_3 = Math.min(int_3, blockPos_2.getY());
                                int_4 = Math.min(int_4, blockPos_2.getZ());
                                int_5 = Math.max(int_5, blockPos_2.getX());
                                int_6 = Math.max(int_6, blockPos_2.getY());
                                int_7 = Math.max(int_7, blockPos_2.getZ());
                                list_3.add(com.mojang.datafixers.util.Pair.of(blockPos_2, structure$StructureBlockInfo_1.tag));
                                if (structure$StructureBlockInfo_1.tag != null) {
                                    blockEntity_3 = iWorld_1.getBlockEntity(blockPos_2);
                                    if (blockEntity_3 != null) {
                                        structure$StructureBlockInfo_1.tag.putInt("x", blockPos_2.getX());
                                        structure$StructureBlockInfo_1.tag.putInt("y", blockPos_2.getY());
                                        structure$StructureBlockInfo_1.tag.putInt("z", blockPos_2.getZ());
                                        blockEntity_3.fromTag(structure$StructureBlockInfo_1.tag);
                                        blockEntity_3.applyMirror(structurePlacementData_1.getMirror());
                                        blockEntity_3.applyRotation(structurePlacementData_1.getRotation());
                                    }
                                }

                        if (fluidState_1 != null && blockState_1.getBlock() instanceof FluidFillable) {
                            ((FluidFillable)blockState_1.getBlock()).tryFillWithFluid(iWorld_1, blockPos_2, blockState_1, fluidState_1);
                            if (!fluidState_1.isStill()) {
                                list_2.add(blockPos_2);
                            }
                        }
                            }
                        }
                    } else {
                        return;
                        //return false;
                    }
                }
            });
        }
        return true;
    }

    public boolean loadFromFile(IWorld world, Identifier identifier){
        if (world instanceof ServerWorld) {
            try {
                ServerWorld serverWorld = (ServerWorld) world;
                StructureManager structureManager = serverWorld.getStructureManager();
                Structure islandStructure = structureManager.getStructure(identifier);
                //ChunkStructure chunkIslandStructure = new ChunkStructure(islandStructure);
                StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setMirrored(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true).setChunkPosition((ChunkPos) null);

                BlockPos position = new BlockPos(baseChunk.getStartX(), 0, baseChunk.getStartZ());
                safePlace((IStructureExtension)islandStructure, world, position, structurePlacementData_1);
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
