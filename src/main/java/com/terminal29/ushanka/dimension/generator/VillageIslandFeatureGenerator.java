package com.terminal29.ushanka.dimension.generator;

import com.google.common.collect.ImmutableMap;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.loot.LootTables;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class VillageIslandFeatureGenerator {

    public static void addPieces(StructureManager structureManager_1, BlockPos blockPos_1, BlockRotation blockRotation_1, List<StructurePiece> list_1, Random random_1, DefaultFeatureConfig defaultFeatureConfig_1) {
        list_1.add(new VillageIslandFeatureGenerator.Piece(structureManager_1, ModInfo.identifierFor(ModInfo.ISLAND_STRUCTURE_0), blockPos_1, blockRotation_1));
    }


    public static class Piece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier identifier;

        public Piece(StructureManager structureManager_1, CompoundTag compoundTag_1) {
            super(UshankaDimensions.VILLAGE_ISLAND_STRUCTURE_PIECE_TYPE, compoundTag_1);

            this.identifier = new Identifier(compoundTag_1.getString("Template"));
            this.rotation = BlockRotation.NONE;

            this.setStructureData(structureManager_1);
        }

        public Piece(StructureManager structureManager, Identifier identifier, BlockPos pos, BlockRotation rotation)
        {
            super(UshankaDimensions.VILLAGE_ISLAND_STRUCTURE_PIECE_TYPE, 0);

            this.rotation = rotation;
            this.identifier = identifier;
            this.pos = pos;

            this.setStructureData(structureManager);
        }

        public void setStructureData(StructureManager structureManager)
        {
            Structure structure_1 = structureManager.getStructureOrBlank(this.identifier);
            StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        protected void handleMetadata(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableIntBoundingBox mutableIntBoundingBox)
        {

        }

        public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1) {
            if(VillageIslandManager.INSTANCE.isIslandChunk(chunkPos_1)){
                VillageIslandManager.INSTANCE.chunkToIsland(chunkPos_1).buildInChunk(iWorld_1);
            }
            return true;
        }
    }
}
