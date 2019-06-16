package com.terminal29.ushanka.utility;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket;
import net.minecraft.entity.EntitySize;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class MovementUtility {

    // Moves a player to a block if it looks like the player could stand on it from their viewpoint
    public static void MoveToVisibleBlock(ServerPlayerEntity player) {
        System.out.println("Checking BlockPos");
        BlockPos currentBlockPos = player.getBlockPos();
        BlockPos feetBlock = currentBlockPos.down();

        VillageIsland playerIsland = VillageIslandManager.INSTANCE.chunkToIsland(player.world.getChunk(player.getBlockPos()).getPos());
        if(playerIsland == null)
            return;
        IsoCameraDirection direction = ((IServerPlayerEntityExtension)player).getCameraDirection();
        BoundingBox islandBB = playerIsland.getVillageBounds();

        Pair<BlockPos, BlockPos> zSnapBounds = getZSnapBoundsForDirection(feetBlock, islandBB, direction);
        if(zSnapBounds == null)
            return;

        // Handle plane collision case
        IsoCameraDirection edgeZSnapDirection;
        if((edgeZSnapDirection = shouldEdgeZSnap(player, islandBB, direction)) != null){
            System.out.println("Edge snapping");
            doEdgeZSnap(player, islandBB, direction, edgeZSnapDirection);

            if(shouldPlaceBarrier(player, islandBB, direction)){
                System.out.println("with barrier.");
            }

        }else {

            // Handle normal z snapping
            BlockPos teleportBase = checkBlockCollision(player, zSnapBounds.getLeft(), zSnapBounds.getRight());
            if (teleportBase != null && !teleportBase.equals(player.getBlockPos().down())) {
                System.out.println("Regular z snap occurring");
                BlockPos teleportLegs = teleportBase.up();
                BlockPos teleportTorso = teleportLegs.up();
                if (player.world.getBlockState(teleportLegs).isAir() && player.world.getBlockState(teleportTorso).isAir()) {
                    System.out.println("Moving to " + currentBlockPos + " : " + feetBlock);
                    BlockPos newPosition = teleportLegs;
                    teleportSmooth(player, newPosition, direction);
                }
            }
        }
    }

    private static void doEdgeZSnap(ServerPlayerEntity player, BoundingBox islandBB, IsoCameraDirection direction, IsoCameraDirection edgeZSnapDirection){
        Pair<BlockPos, BlockPos> edgeSnapBounds;
        BlockPos snapTarget;
        // Handle north-south perspective
        if(direction == IsoCameraDirection.NORTH || direction == IsoCameraDirection.SOUTH){
            if (edgeZSnapDirection == IsoCameraDirection.EAST) {
                edgeSnapBounds = getZSnapBoundsForDirection(player.getBlockPos().east(), islandBB, direction);
            } else {
                edgeSnapBounds = getZSnapBoundsForDirection(player.getBlockPos().west(), islandBB, direction);
            }
            snapTarget = checkBlockCollision(player, edgeSnapBounds.getLeft(), edgeSnapBounds.getRight());
            if (snapTarget != null) {
                if(direction == IsoCameraDirection.NORTH){
                    if (edgeZSnapDirection == IsoCameraDirection.EAST) {
                        teleportSmooth(player, snapTarget.south().west(), direction);
                    } else {
                        teleportSmooth(player, snapTarget.south().east(), direction);
                    }
                }else{
                    if (edgeZSnapDirection == IsoCameraDirection.EAST) {
                        teleportSmooth(player, snapTarget.north().west(), direction);
                    } else {
                        teleportSmooth(player, snapTarget.north().east(), direction);
                    }
                }
            }
        }
        if(direction == IsoCameraDirection.EAST || direction == IsoCameraDirection.WEST){
            if (edgeZSnapDirection == IsoCameraDirection.NORTH) {
                edgeSnapBounds = getZSnapBoundsForDirection(player.getBlockPos().north(), islandBB, direction);
            } else {
                edgeSnapBounds = getZSnapBoundsForDirection(player.getBlockPos().south(), islandBB, direction);
            }
            snapTarget = checkBlockCollision(player, edgeSnapBounds.getLeft(), edgeSnapBounds.getRight());
            if (snapTarget != null) {
                if(direction == IsoCameraDirection.EAST){
                    if (edgeZSnapDirection == IsoCameraDirection.NORTH) {
                        teleportSmooth(player, snapTarget.west().south(), direction);
                    } else {
                        teleportSmooth(player, snapTarget.west().north(), direction);
                    }
                }else{
                    if (edgeZSnapDirection == IsoCameraDirection.NORTH) {
                        teleportSmooth(player, snapTarget.east().south(), direction);
                    } else {
                        teleportSmooth(player, snapTarget.east().north(), direction);
                    }
                }
            }
        }
    }

    private static void teleportSmooth(ServerPlayerEntity player, BlockPos newPosition, IsoCameraDirection direction){
        Vec3d offset = player.getPos();
        offset = offset.subtract(player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ());

        Vec3d newPos = new Vec3d(
                newPosition.getX() + ((direction == IsoCameraDirection.NORTH || direction == IsoCameraDirection.SOUTH) ? offset.getX() : 0.5),
                newPosition.getY() + offset.getY(),
                newPosition.getZ() + ((direction == IsoCameraDirection.EAST || direction == IsoCameraDirection.WEST) ? offset.getZ() : 0.5));

        //player.teleport((ServerWorld)player.world, newPos.getX(), newPos.getY(), newPos.getZ(), player.yaw, player.pitch);
        player.networkHandler.teleportRequest(newPos.getX(), newPos.getY(), newPos.getZ(), player.yaw, player.pitch, EnumSet.allOf(PlayerPositionLookS2CPacket.Flag.class));

    }

    private static IsoCameraDirection shouldEdgeZSnap(ServerPlayerEntity player, BoundingBox islandBB, IsoCameraDirection direction){
        Vec3d blockOffset = player.getPos();
        BlockPos currentBlockPos = player.getBlockPos();
        blockOffset = blockOffset.subtract(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ());
        float margin = 0.05f;

        switch(direction) {
            case NORTH:
            // Check right
            if (blockOffset.x > 0.55) {
                Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().east(), islandBB, direction);
                BlockPos rightWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                if (rightWall != null && rightWall.getZ() >= player.getBlockPos().getZ())
                    return IsoCameraDirection.EAST;
                return null;
            } else if(blockOffset.x < 0.45){
                Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().west(), islandBB, direction);
                BlockPos leftWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                if (leftWall != null && leftWall.getZ() >= player.getBlockPos().getZ())
                    return IsoCameraDirection.WEST;
                return null;
            }
            break;
            case SOUTH:
                // Check right
                if (blockOffset.x > 0.55) {
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().east(), islandBB, direction);
                    BlockPos rightWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (rightWall != null && rightWall.getZ() <= player.getBlockPos().getZ())
                        return IsoCameraDirection.EAST;
                    return null;
                } else if(blockOffset.x < 0.45){
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().west(), islandBB, direction);
                    BlockPos leftWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (leftWall != null && leftWall.getZ() <= player.getBlockPos().getZ())
                        return IsoCameraDirection.WEST;
                    return null;
                }
                break;
            case EAST:
                // Check right
                if (blockOffset.z > 0.55) {
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().south(), islandBB, direction);
                    BlockPos rightWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (rightWall != null && rightWall.getX() <= player.getBlockPos().getX())
                        return IsoCameraDirection.SOUTH;
                    return null;
                } else if(blockOffset.z < 0.45){
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().north(), islandBB, direction);
                    BlockPos leftWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (leftWall != null && leftWall.getX() <= player.getBlockPos().getX())
                        return IsoCameraDirection.NORTH;
                    return null;
                }
                break;
            case WEST:
                // Check right
                if (blockOffset.z > 0.55) {
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().south(), islandBB, direction);
                    BlockPos rightWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (rightWall != null && rightWall.getX() >= player.getBlockPos().getX())
                        return IsoCameraDirection.SOUTH;
                    return null;
                } else if(blockOffset.z < 0.45){
                    Pair<BlockPos, BlockPos> rightCollisionBounds = getZSnapBoundsForDirection(player.getBlockPos().north(), islandBB, direction);
                    BlockPos leftWall = checkBlockCollision(player, rightCollisionBounds.getLeft(), rightCollisionBounds.getRight());
                    if (leftWall != null && leftWall.getX() >= player.getBlockPos().getX())
                        return IsoCameraDirection.NORTH;
                    return null;
                }
                break;
        }
        return null;
    }

    private static boolean shouldPlaceBarrier(ServerPlayerEntity player, BoundingBox islandBB, IsoCameraDirection direction){
        return false;
    }

    // returns leg position where a collision may occur
    @Nullable
    private static BlockPos checkPlaneCollisionForDirection(ServerPlayerEntity player, BoundingBox islandBB, IsoCameraDirection direction) {
        //EntitySize playerSize = player.getSize(player.getPose());
        BoundingBox playerBox = player.getCollisionBox();
        Vec3d blockOffset = player.getPos();
        BlockPos currentBlockPos = player.getBlockPos();
        blockOffset = blockOffset.subtract(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ());
        BlockPos finalCollision = null;

        switch(direction){
            // X plane, + to - z
            case NORTH:
                Pair<BlockPos, BlockPos> zSnapBounds = getZSnapBoundsForDirection(currentBlockPos, islandBB, direction);
                if(blockOffset.getX() < 0.5){
                    // check left side blocks
                    BlockPos torsoCollision = checkBlockCollision(player, zSnapBounds.getLeft().west().up(), zSnapBounds.getRight().west().up());

                }else{
                    // check left side blocks
                    BlockPos torsoCollision = checkBlockCollision(player, zSnapBounds.getLeft().east().up(), zSnapBounds.getRight().east().up());

                }
        }
        return finalCollision;
    }

    @Nullable
    private static Pair<BlockPos, BlockPos> getZSnapBoundsForDirection(BlockPos baseBlock, BoundingBox islandBB, IsoCameraDirection direction){
        switch(direction){
            case NORTH:
                return new Pair<>(new BlockPos(baseBlock.getX(), baseBlock.getY(), islandBB.maxZ), new BlockPos(baseBlock.getX(), baseBlock.getY(), islandBB.minZ));
            case SOUTH:
                return new Pair<>(new BlockPos(baseBlock.getX(), baseBlock.getY(), islandBB.minZ), new BlockPos(baseBlock.getX(), baseBlock.getY(), islandBB.maxZ));
            case EAST:
                return new Pair<>(new BlockPos(islandBB.minX, baseBlock.getY(), baseBlock.getZ()), new BlockPos(islandBB.maxX, baseBlock.getY(), baseBlock.getZ()));
            case WEST:
                return new Pair<>(new BlockPos(islandBB.maxX, baseBlock.getY(), baseBlock.getZ()), new BlockPos(islandBB.minX, baseBlock.getY(), baseBlock.getZ()));
        }
        return null;
    }

    @Nullable
    private static BlockPos checkBlockCollision(ServerPlayerEntity player, BlockPos start, BlockPos end){
        int xDir = start.getX() < end.getX() ? 1 : -1;
        int yDir = start.getY() < end.getY() ? 1 : -1;
        int zDir = start.getZ() < end.getZ() ? 1 : -1;

        for(int x = start.getX(); MathUtilities.isInRangeInclusive(x, start.getX(), end.getX()); x += xDir){
            for(int y = start.getY(); MathUtilities.isInRangeInclusive(y, start.getY(), end.getY()); y += yDir){
                for(int z = start.getZ(); MathUtilities.isInRangeInclusive(z, start.getZ(), end.getZ()); z+= zDir){
                    BlockPos pos = new BlockPos(x,y,z);
                    boolean isAir = player.world.getBlockState(pos).isAir();
                    if(!isAir)
                        return pos;
                }
            }
        }
        return null;
    }
}
