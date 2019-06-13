package com.terminal29.ushanka.mixin;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.extension.IStructureExtension;
import net.minecraft.structure.Structure;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(Structure.class)
public abstract class MixinStructure implements IStructureExtension {

    @Shadow
    @Final
    List<List<Structure.StructureBlockInfo>> blocks;

    @Shadow
    @Final
    List<Structure.StructureEntityInfo> entities;

    @Shadow
    BlockPos size;

    @Shadow
    abstract void method_15179(IWorld iWorld_1, BlockPos blockPos_1, BlockMirror blockMirror_1, BlockRotation blockRotation_1, BlockPos blockPos_2, @Nullable MutableIntBoundingBox mutableIntBoundingBox_1);

    @Override
    public List<Structure.StructureEntityInfo> getEntities() {
        return entities;
    }

    @Override
    public List<List<Structure.StructureBlockInfo>> getBlocks() {
        return blocks;
    }

    @Override
    public BlockPos getSize() {
        return size;
    }

    @Override
    public void method_15179_accessor(IWorld iWorld_1, BlockPos blockPos_1, BlockMirror blockMirror_1, BlockRotation blockRotation_1, BlockPos blockPos_2, MutableIntBoundingBox mutableIntBoundingBox_1) {
        method_15179(iWorld_1, blockPos_1, blockMirror_1, blockRotation_1, blockPos_2, mutableIntBoundingBox_1);
    }
}
