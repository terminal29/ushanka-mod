package com.terminal29.ushanka.block;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

public class HypergateBlockEntity extends BlockEntity implements Tickable {

    public HypergateBlockEntity() {
        super(UshankaBlocks.HYPERGATE_BLOCK_ENTITY_TYPE);
    }

    @Override
    public void tick() {

    }

    public static boolean shouldDrawSide(BlockState state, Direction direction){
        return state.get(Properties.FACING).equals(direction);
    }

    @Nullable
    public VillageIsland getTargetIsland(){
        Direction direction = getCachedState().get(Properties.FACING);
        VillageIsland island = VillageIslandManager.INSTANCE.chunkToIsland(new ChunkPos(this.getPos()));
        if(island == null) return null;
        ChunkPos thisIsland = island.getBaseChunkPos();
        ChunkPos targetIslandPos;
        switch(direction){
            case NORTH:
                targetIslandPos = new ChunkPos(thisIsland.x, thisIsland.z + VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING);
                break;
            case SOUTH:
                targetIslandPos = new ChunkPos(thisIsland.x, thisIsland.z - VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - VillageIslandManager.ISLAND_CHUNK_SPACING);
                break;
            case EAST:
                targetIslandPos = new ChunkPos(thisIsland.x - VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH - VillageIslandManager.ISLAND_CHUNK_SPACING, thisIsland.z);
                break;
            case WEST:
                targetIslandPos = new ChunkPos(thisIsland.x + VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH + VillageIslandManager.ISLAND_CHUNK_SPACING, thisIsland.z);
                break;
            default:
                return null;
        }

        return VillageIslandManager.INSTANCE.chunkToIsland(targetIslandPos);
    }
}
