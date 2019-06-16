package com.terminal29.ushanka.utility;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.core.jmx.Server;

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
        IClientPlayerEntityExtension.CameraDirection direction = ((IServerPlayerEntityExtension)player).getCameraDirection();
        BoundingBox islandBB = playerIsland.getVillageBounds();
        BlockPos islandPos = playerIsland.getBaseChunkPos().toBlockPos(0,0,0);
        BlockPos searchStart = null, searchEnd = null;
        switch(direction){
            case NORTH:
                searchStart = new BlockPos(feetBlock.getX(), feetBlock.getY(), islandBB.maxZ);
                searchEnd = new BlockPos(feetBlock.getX(), feetBlock.getY(), islandBB.minZ);
                break;
            case SOUTH:
                searchStart = new BlockPos(feetBlock.getX(), feetBlock.getY(), islandBB.minZ);
                searchEnd = new BlockPos(feetBlock.getX(), feetBlock.getY(), islandBB.maxZ);
                break;
            case EAST:
                searchStart = new BlockPos(islandBB.minX, feetBlock.getY(), feetBlock.getZ());
                searchEnd = new BlockPos(islandBB.maxX, feetBlock.getY(), feetBlock.getZ());
                break;
            case WEST:
                searchStart = new BlockPos(islandBB.maxX, feetBlock.getY(), feetBlock.getZ());
                searchEnd = new BlockPos(islandBB.minX, feetBlock.getY(), feetBlock.getZ());
                break;
        }
        if(searchStart == null || searchEnd == null)
            return;

        BlockPos teleportBase = null;
       // if() {
       //     System.out.println("Plane collision incoming, z snapping forward.");

       /* }else*/ if((teleportBase = checkBlockCollision(player, searchStart, searchEnd)) != null && !teleportBase.equals(player.getBlockPos().down())){
            System.out.println("Regular z snap occurring");
            BlockPos teleportLegs = teleportBase.up();
            BlockPos teleportTorso = teleportLegs.up();
            if (player.world.getBlockState(teleportLegs).isAir() && player.world.getBlockState(teleportTorso).isAir()) {


                System.out.println("Moving to " + currentBlockPos + " : " + feetBlock);
                BlockPos newPosition = teleportLegs;
                Vec3d offset = player.getPos();
                offset = offset.subtract(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ());

                Vec3d newPos = new Vec3d(
                        newPosition.getX() + ((direction == IClientPlayerEntityExtension.CameraDirection.NORTH || direction == IClientPlayerEntityExtension.CameraDirection.SOUTH) ? offset.getX() : 0.5),
                        newPosition.getY() + offset.getY(),
                        newPosition.getZ() + ((direction == IClientPlayerEntityExtension.CameraDirection.EAST || direction == IClientPlayerEntityExtension.CameraDirection.WEST) ? offset.getZ() : 0.5));

                //player.teleport((ServerWorld)player.world, newPos.getX(), newPos.getY(), newPos.getZ(), player.yaw, player.pitch);
                player.networkHandler.teleportRequest(newPos.getX(), newPos.getY(), newPos.getZ(), player.yaw, player.pitch, EnumSet.allOf(PlayerPositionLookS2CPacket.Flag.class));

            }
        }
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
