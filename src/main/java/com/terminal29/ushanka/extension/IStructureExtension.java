package com.terminal29.ushanka.extension;

import com.sun.istack.internal.Nullable;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;

import java.util.List;

public interface IStructureExtension {
    List<List<Structure.StructureBlockInfo>> getBlocks();

    List<Structure.StructureEntityInfo> getEntities();

    BlockPos getSize();

     void method_15179_accessor(IWorld iWorld_1, BlockPos blockPos_1, BlockMirror blockMirror_1, BlockRotation blockRotation_1, BlockPos blockPos_2, @Nullable MutableIntBoundingBox mutableIntBoundingBox_1);
}
