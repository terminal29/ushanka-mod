package com.terminal29.ushanka.mixin;

import com.mojang.authlib.GameProfile;
import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.UshankaPersistentData;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

import static com.terminal29.ushanka.extension.IClientPlayerEntityExtension.CameraDirection;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IServerPlayerEntityExtension, IPlayerEntityExtension {
    private static final int teleportSearchDistance = 20;
    boolean isCameraIso = true;

    CameraDirection cameraDirection = CameraDirection.NONE;

    DimensionType previousDimension = null;

    BlockPos previousPosition = null;

    BlockPos previousBlockPos = new BlockPos(0, 0, 0);

    public MixinServerPlayerEntity(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftServer minecraftServer_1, ServerWorld serverWorld_1, GameProfile gameProfile_1, ServerPlayerInteractionManager serverPlayerInteractionManager_1, CallbackInfo cbi) {

    }

    private ServerPlayerEntity asSPEntity() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Override
    public void onClientReady() {
        UshankaPersistentData persistentData = UshankaPersistentData.get(getServer());
        updateClientIsoState(persistentData.getPlayerIsoState(this.uuid));
        updateClientIsoDirection(persistentData.getPlayerIsoDirection(this.uuid));
        this.onCameraIsoChanged(persistentData.getPlayerIsoState(this.uuid), false);
        this.onCameraDirectionChanged(persistentData.getPlayerIsoDirection(this.uuid), false);


    }

    private void updateClientIsoState(boolean state) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        buf.writeUuid(this.getUuid());
        ((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModInfo.identifierFor(ModInfo.PACKET_ISO_STATE), buf));
    }

    private void updateClientIsoDirection(CameraDirection direction) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(direction.name());
        buf.writeUuid(this.getUuid());
        ((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModInfo.identifierFor(ModInfo.PACKET_ISO_DIRECTION), buf));
    }

    public void onCameraIsoChanged(boolean state, boolean updateRemote) {
        if (updateRemote)
            updateClientIsoState(state);

        isCameraIso = state;
        UshankaPersistentData.get(getServer()).setPlayerIsoState(this.uuid, this.isCameraIso);
        if (state && world.dimension.getType() != UshankaDimensions.VILLAGE) {
            onDimensionChanged(UshankaDimensions.VILLAGE);
        }
    }

    public void onCameraDirectionChanged(IClientPlayerEntityExtension.CameraDirection direction, boolean updateRemote) {
        if (updateRemote)
            updateClientIsoDirection(direction);
        cameraDirection = direction;
        UshankaPersistentData.get(getServer()).setPlayerIsoDirection(this.uuid, this.cameraDirection);
    }

    public void onDimensionChanged(DimensionType type) {
        // Save state to persistent data
        UshankaPersistentData.get(getServer()).setPlayerIsoState(this.uuid, this.isCameraIso);
        UshankaPersistentData.get(getServer()).setPlayerIsoDirection(this.uuid, this.cameraDirection);
        previousDimension = this.dimension;
        previousPosition = this.getBlockPos();
        changeToDimension(type);
    }

    @Override
    public boolean isCameraIso() {
        return isCameraIso;
    }

    @Override
    public CameraDirection getCameraDirection() {
        return this.cameraDirection;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        if(isCameraIso()) {
            if (!this.getBlockPos().isWithinDistance(previousBlockPos, 1)) {
                System.out.println(this.getBlockPos());
                previousBlockPos = this.getBlockPos();
                MoveToVisibleBlock();
            }
        }
    }

    // Moves a player to a block if it looks like the player could stand on it from their viewpoint
    private void MoveToVisibleBlock() {
        System.out.println("Checking BlockPos");
        BlockPos currentBlockPos = this.getBlockPos();
        BlockPos currentSteppingBlock = collidesInRange(0, 0, true, currentBlockPos.down());
        if (currentSteppingBlock == null) {
            switch (getCameraDirection()) {
                case NORTH: {
                    // Check there is a block we can teleport to
                    BlockPos feetBlock = collidesInRange(teleportSearchDistance, -teleportSearchDistance, true, currentBlockPos.down());
                    if (feetBlock != null) {
                        // That we can fit at
                        BlockPos legsBlock = collidesInRange(teleportSearchDistance, 0, true, feetBlock.up(1));
                        BlockPos torsoBlock = collidesInRange(teleportSearchDistance, 0, true, feetBlock.up(2));
                        if (legsBlock == null && torsoBlock == null) {
                            System.out.println("Found Space to move to " + currentBlockPos + " : " + feetBlock);
                            BlockPos newPosition = feetBlock.up();
                            asSPEntity().networkHandler.teleportRequest(this.x, this.y, newPosition.getZ(), this.yaw, this.pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                        }
                    }
                    break;
                }
                case SOUTH: {
                    BlockPos feetBlock = collidesInRange(-teleportSearchDistance, teleportSearchDistance, true, currentBlockPos.down());
                    if (feetBlock != null) {
                        // That we can fit at
                        BlockPos legsBlock = collidesInRange(-teleportSearchDistance, 0, true, feetBlock.up(1));
                        BlockPos torsoBlock = collidesInRange(-teleportSearchDistance, 0, true, feetBlock.up(2));
                        if (legsBlock == null && torsoBlock == null) {
                            System.out.println("Found Space to move to " + currentBlockPos + " : " + feetBlock);
                            BlockPos newPosition = feetBlock.up();
                            asSPEntity().networkHandler.teleportRequest(this.x, this.y, newPosition.getZ(), this.yaw, this.pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                        }
                    }
                    break;
                }
                case EAST: {
                    BlockPos feetBlock = collidesInRange(-teleportSearchDistance, teleportSearchDistance, false, currentBlockPos.down());
                    if (feetBlock != null) {
                        // That we can fit at
                        BlockPos legsBlock = collidesInRange(-teleportSearchDistance, 0, false, feetBlock.up(1));
                        BlockPos torsoBlock = collidesInRange(-teleportSearchDistance, 0, false, feetBlock.up(2));
                        if (legsBlock == null && torsoBlock == null) {
                            System.out.println("Found Space to move to " + currentBlockPos + " : " + feetBlock);
                            BlockPos newPosition = feetBlock.up();
                            asSPEntity().networkHandler.teleportRequest(newPosition.getX(), this.y, this.z, this.yaw, this.pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                        }
                    }
                    break;
                }
                case WEST: {
                    BlockPos feetBlock = collidesInRange(teleportSearchDistance, -teleportSearchDistance, false, currentBlockPos.down());
                    if (feetBlock != null) {
                        // That we can fit at
                        BlockPos legsBlock = collidesInRange(teleportSearchDistance, 0, false, feetBlock.up(1));
                        BlockPos torsoBlock = collidesInRange(teleportSearchDistance, 0, false, feetBlock.up(2));
                        if (legsBlock == null && torsoBlock == null) {
                            System.out.println("Found Space to move to " + currentBlockPos + " : " + feetBlock);
                            BlockPos newPosition = feetBlock.up();
                            asSPEntity().networkHandler.teleportRequest(newPosition.getX(), this.y, this.z, this.yaw, this.pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                        }
                    }
                    break;
                }
            }
        }

    }

    @Nullable
    private BlockPos collidesInRange(int relativeStart, int relativeEnd, boolean searchZAxis, BlockPos middlePoint) {
        int i = relativeStart;
        while (true) {
            BlockPos searchPos = new BlockPos(searchZAxis ? middlePoint.getX() : middlePoint.getX() + i, middlePoint.getY(), searchZAxis ? middlePoint.getZ() + i : middlePoint.getZ());
            if (!this.world.getBlockState(searchPos).isAir())
                return searchPos;
            i += relativeStart < relativeEnd ? 1 : -1;
            if (relativeStart < relativeEnd ? i >= relativeEnd : i <= relativeEnd)
                break;
        }
        return null;
    }

}
