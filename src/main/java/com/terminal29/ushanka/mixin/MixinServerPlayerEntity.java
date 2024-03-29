package com.terminal29.ushanka.mixin;

import com.mojang.authlib.GameProfile;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.UshankaPersistentData;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import com.terminal29.ushanka.utility.MovementUtility;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.terminal29.ushanka.utility.IsoCameraDirection;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IServerPlayerEntityExtension, IPlayerEntityExtension {
    private static final int teleportSearchDistance = 20;
    boolean isCameraIso = true;

    IsoCameraDirection cameraDirection = IsoCameraDirection.NONE;

    DimensionType previousDimension = null;

    Vec3d previousPosition = new Vec3d(0,0,0);
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

    private void updateClientIsoDirection(IsoCameraDirection direction) {
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

    public void onCameraDirectionChanged(IsoCameraDirection direction, boolean updateRemote) {
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
        previousBlockPos = this.getBlockPos();
        changeToDimension(type);
    }

    @Override
    public boolean isCameraIso() {
        return isCameraIso;
    }

    @Override
    public IsoCameraDirection getCameraDirection() {
        return this.cameraDirection;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        if(isCameraIso()) {
            if (this.getPos().distanceTo(previousPosition) > 0.05 || this.isSneaking()) {
                previousPosition = this.getPos();
                MovementUtility.MoveToVisibleBlock(asSPEntity());
            }
        }
    }
}
