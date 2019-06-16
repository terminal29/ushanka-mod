package com.terminal29.ushanka.mixin;

import com.mojang.authlib.GameProfile;
import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IGameRenderExtension;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.utility.IsoCameraDirection;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipe.book.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements IClientPlayerEntityExtension, IPlayerEntityExtension {


    private IsoCameraDirection currentDirection = IsoCameraDirection.NONE;
    private IsoCameraDirection requestedDirection = IsoCameraDirection.NONE;
    private boolean isChangingDirection = false;
    private float currentIsoScale = 25;
    private float requestedIsoScale = 25;
    private float isoSlider = 0;
    private float currentIsoDistance = 0.4f;
    private float requestedIsoDistance = 0.4f;
    private boolean isCameraIso = false;
    private boolean requestedCameraIso = false;
    private boolean isCameraAnimatingIsoChange = false;
    private boolean clientReady = false;

    public MixinClientPlayerEntity(ClientWorld clientWorld_1, GameProfile gameProfile_1) {
        super(clientWorld_1, gameProfile_1);
    }

    @Override
    public void onCameraIsoChanged(boolean state, boolean updateRemote){
        this.isCameraIso = requestedCameraIso = state;
        this.isoSlider = isCameraIso ? 1 : 0;
        if(updateRemote)
            updateServerIsoState(state);
    }

    @Override
    public void onCameraDirectionChanged(IsoCameraDirection direction, boolean updateRemote){
        this.currentDirection = this.requestedDirection =  direction;
        if(updateRemote)
            updateServerIsoDirection(direction);
    }

    private void updateServerIsoState(boolean state){
        System.out.println(ModInfo.PACKET_ISO_STATE + " " + state);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        buf.writeUuid(this.getUuid());
        ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadC2SPacket(new Identifier(ModInfo.DISPLAY_NAME, ModInfo.PACKET_ISO_STATE), buf));
    }

    private void updateServerIsoDirection(IsoCameraDirection direction){
        System.out.println(ModInfo.PACKET_ISO_DIRECTION + " " + direction.name());
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(direction.name());
        buf.writeUuid(this.getUuid());
        ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadC2SPacket(ModInfo.identifierFor(ModInfo.PACKET_ISO_DIRECTION), buf));
    }

    private void tellServerClientIsReady(){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(true);
        buf.writeUuid(this.getUuid());
        ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new CustomPayloadC2SPacket(ModInfo.identifierFor(ModInfo.PACKET_CLIENT_READY), buf));
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler statHandler, ClientRecipeBook recipeBook, CallbackInfo cbi) {
        IGameRenderExtension gameRenderExtension = (IGameRenderExtension) MinecraftClient.getInstance().gameRenderer;
        gameRenderExtension.addOnRenderEventHandler(e -> {
            if (requestedCameraIso != isCameraIso) {
                isCameraAnimatingIsoChange = true;
                if (requestedCameraIso) {
                    isoSlider = MathUtilities.lerp(isoSlider, 1, 0.1f);
                    this.pitch = MathUtilities.lerp(this.pitch, 0, isoSlider);
                    if (Math.abs(1 - isoSlider) < ISO_DEADZONE) {
                        isoSlider = 1;
                        this.onCameraIsoChanged(true, true);
                    }
                    if ((requestedDirection == IsoCameraDirection.NONE)) {
                        requestedDirection = getClosestCameraDirection(this.yaw + 90); // not sure why i need this
                    }
                    rotateToDirection();
                } else {
                    isoSlider = MathUtilities.lerp(isoSlider, 0, 0.2f);
                    if (Math.abs(isoSlider) < ISO_DEADZONE) {
                        isoSlider = 0;
                        this.onCameraIsoChanged(false, true);
                        requestedDirection = IsoCameraDirection.NONE;
                        this.onCameraDirectionChanged(IsoCameraDirection.NONE, true);
                    }
                }
            } else {
                isCameraAnimatingIsoChange = false;
            }

            if (isCameraIso) {
                currentIsoScale = requestedIsoScale;
                currentIsoDistance = requestedIsoDistance;
                rotateToDirection();
            }
        });
    }



    @Inject(method = "tick", at = @At("RETURN"))
    protected void onTick(CallbackInfo info) {
        if(!clientReady){
            clientReady = true;
            tellServerClientIsReady();
        }
    }

    private float angleForDirection(IsoCameraDirection direction) {
        if (direction == IsoCameraDirection.NORTH)
            return 180;
        if (direction == IsoCameraDirection.SOUTH)
            return 0;
        if (direction == IsoCameraDirection.EAST)
            return 270;
        if (direction == IsoCameraDirection.WEST)
            return 90;
        return Float.POSITIVE_INFINITY;
    }

    private void rotateToDirection() {
        if (requestedDirection == IsoCameraDirection.NORTH && currentDirection != IsoCameraDirection.NORTH) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, angleForDirection(IsoCameraDirection.NORTH), 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, angleForDirection(IsoCameraDirection.NORTH))) < YAW_DEADZONE) {
                this.yaw = angleForDirection(IsoCameraDirection.NORTH);
                this.onCameraDirectionChanged(IsoCameraDirection.NORTH, true);
                isChangingDirection = false;
            }
        }
        if (requestedDirection == IsoCameraDirection.SOUTH && currentDirection != IsoCameraDirection.SOUTH) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, angleForDirection(IsoCameraDirection.SOUTH), 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, angleForDirection(IsoCameraDirection.SOUTH))) < YAW_DEADZONE) {
                this.yaw = angleForDirection(IsoCameraDirection.SOUTH);
                this.onCameraDirectionChanged(IsoCameraDirection.SOUTH, true);
                isChangingDirection = false;
            }
        }
        if (requestedDirection == IsoCameraDirection.EAST && currentDirection != IsoCameraDirection.EAST) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, angleForDirection(IsoCameraDirection.EAST), 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, angleForDirection(IsoCameraDirection.EAST))) < YAW_DEADZONE) {
                this.yaw = angleForDirection(IsoCameraDirection.EAST);
                this.onCameraDirectionChanged(IsoCameraDirection.EAST, true);
                isChangingDirection = false;
            }
        }
        if (requestedDirection == IsoCameraDirection.WEST && currentDirection != IsoCameraDirection.WEST) {
            isChangingDirection = true;
            this.yaw = MathUtilities.angleLerp(this.yaw, angleForDirection(IsoCameraDirection.WEST), 0.2f);
            if (Math.abs(MathUtilities.shortAngleDist(this.yaw, angleForDirection(IsoCameraDirection.WEST))) < YAW_DEADZONE) {
                this.yaw = angleForDirection(IsoCameraDirection.WEST);
                this.onCameraDirectionChanged(IsoCameraDirection.WEST, true);
                isChangingDirection = false;
            }
        }
    }

    private IsoCameraDirection getClosestCameraDirection(float angle) {
        float northDistance = MathUtilities.shortAngleDist(angle, angleForDirection(IsoCameraDirection.NORTH));
        float eastDistance = MathUtilities.shortAngleDist(angle, angleForDirection(IsoCameraDirection.EAST));
        float westDistance = MathUtilities.shortAngleDist(angle, angleForDirection(IsoCameraDirection.WEST));
        float southDistance = MathUtilities.shortAngleDist(angle, angleForDirection(IsoCameraDirection.SOUTH));

        List<Pair<Float, IsoCameraDirection>> directions = new ArrayList<>();
        directions.add(new Pair<>(northDistance, IsoCameraDirection.NORTH));
        directions.add(new Pair<>(eastDistance, IsoCameraDirection.EAST));
        directions.add(new Pair<>(westDistance, IsoCameraDirection.WEST));
        directions.add(new Pair<>(southDistance, IsoCameraDirection.SOUTH));

        directions.sort(Comparator.comparing(Pair::getLeft));

        return directions.get(0).getRight();
    }

    @Override
    public float getIsoScale() {
        return currentIsoScale;
    }

    @Override
    public void setIsoScale(float requestedIsoScale) {
        this.requestedIsoScale = requestedIsoScale;
    }

    @Override
    public float getIsoSlider() {
        return this.isoSlider;
    }

    @Override
    public boolean isChangingDirection() {
        return isChangingDirection;
    }

    @Override
    public IsoCameraDirection getCameraDirection() {
        return currentDirection;
    }

    @Override
    public void requestCameraDirection(IsoCameraDirection direction) {
        this.requestedDirection = direction;
    }

    @Override
    public void forceCameraDirection(IsoCameraDirection direction){
        this.requestedDirection = direction;
        this.onCameraDirectionChanged(direction, true);
    }

    @Override
    public void rotateCameraLeft() {
        if (getCameraDirection() == IsoCameraDirection.NORTH) {
            requestCameraDirection(IsoCameraDirection.WEST);
        } else if (getCameraDirection() == IsoCameraDirection.WEST) {
            requestCameraDirection(IsoCameraDirection.SOUTH);
        } else if (getCameraDirection() == IsoCameraDirection.SOUTH) {
            requestCameraDirection(IsoCameraDirection.EAST);
        } else if (getCameraDirection() == IsoCameraDirection.EAST) {
            requestCameraDirection(IsoCameraDirection.NORTH);
        }
    }

    @Override
    public void rotateCameraRight() {
        if (getCameraDirection() == IsoCameraDirection.NORTH) {
            requestCameraDirection(IsoCameraDirection.EAST);
        } else if (getCameraDirection() == IsoCameraDirection.EAST) {
            requestCameraDirection(IsoCameraDirection.SOUTH);
        } else if (getCameraDirection() == IsoCameraDirection.SOUTH) {
            requestCameraDirection(IsoCameraDirection.WEST);
        } else if (getCameraDirection() == IsoCameraDirection.WEST) {
            requestCameraDirection(IsoCameraDirection.NORTH);
        }
    }

    @Override
    public boolean isCameraIso() {
        return isCameraIso || isCameraAnimatingIsoChange;
    }

    @Override
    public void requestCameraIso(boolean state) {
        if (!isCameraAnimatingIsoChange)
            requestedCameraIso = state;
    }

    @Override
    public void forceCameraIso(boolean state) {
        requestedCameraIso = state;
        this.onCameraIsoChanged(state, true);
    }

    @Override
    public float getIsoDistance() {
        return currentIsoDistance;
    }

    @Override
    public void setIsoDistance(float isoDistance){
        this.requestedIsoDistance = isoDistance;
    }
}