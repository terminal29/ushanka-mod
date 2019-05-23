package com.terminal29.ushanka.mixin;

import com.mojang.authlib.GameProfile;
import static com.terminal29.ushanka.extension.IClientPlayerEntityExtension.CameraDirection;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.UshankaPersistentData;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import io.netty.buffer.Unpooled;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IServerPlayerEntityExtension, IPlayerEntityExtension {
    public MixinServerPlayerEntity(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    boolean isCameraIso = true;

    CameraDirection cameraDirection = CameraDirection.NONE;

    DimensionType previousDimension = null;

    BlockPos previousPosition = null;

    @Inject(method="<init>", at=@At("RETURN"))
    public void init(MinecraftServer minecraftServer_1, ServerWorld serverWorld_1, GameProfile gameProfile_1, ServerPlayerInteractionManager serverPlayerInteractionManager_1, CallbackInfo cbi){

    }

    @Override
    public void onClientReady(){
        UshankaPersistentData persistentData = UshankaPersistentData.get(getServer());
        updateClientIsoState(persistentData.getPlayerIsoState(this.uuid));
        updateClientIsoDirection(persistentData.getPlayerIsoDirection(this.uuid));
    }

    private void updateClientIsoState(boolean state){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        buf.writeUuid(this.getUuid());
        ((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModInfo.identifierFor(ModInfo.Packets.ISO_STATE), buf));
    }

    private void updateClientIsoDirection(CameraDirection direction){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(direction.name());
        buf.writeUuid(this.getUuid());
        ((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModInfo.identifierFor(ModInfo.Packets.ISO_DIRECTION), buf));
    }

    public void onCameraIsoChanged(boolean state, boolean updateRemote){
        if(updateRemote)
            updateClientIsoState(state);

        isCameraIso = state;
        UshankaPersistentData.get(getServer()).setPlayerIsoState(this.uuid, this.isCameraIso);
        if(state && world.dimension.getType() != UshankaDimensions.VILLAGE) {
            onDimensionChanged(UshankaDimensions.VILLAGE);
        }
    }

    public void onCameraDirectionChanged(IClientPlayerEntityExtension.CameraDirection direction, boolean updateRemote){
        if(updateRemote)
            updateClientIsoDirection(direction);
        cameraDirection = direction;
        UshankaPersistentData.get(getServer()).setPlayerIsoDirection(this.uuid, this.cameraDirection);
    }

    public void onDimensionChanged(DimensionType type){
        // Save state to persistent data
        UshankaPersistentData.get(getServer()).setPlayerIsoState(this.uuid, this.isCameraIso);
        UshankaPersistentData.get(getServer()).setPlayerIsoDirection(this.uuid, this.cameraDirection);
        previousDimension = this.dimension;
        previousPosition = this.getBlockPos();
        changeToDimension(type);
    }

    @Override
    public boolean isCameraIso(){
        return isCameraIso;
    }

    @Override
    public CameraDirection getCameraDirection(){
        return this.cameraDirection;
    }
}
