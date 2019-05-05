package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.IGameRenderExtension;
import com.terminal29.ushanka.IPlayerEntityExtension;
import com.terminal29.ushanka.MathUtilities;
import com.terminal29.ushanka.Ushanka;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinEntityPlayer extends LivingEntity implements IPlayerEntityExtension {

    private long ticks = 0;

    private CameraDirection currentDirection = CameraDirection.NORTH;
    private CameraDirection requestedDirection = CameraDirection.NONE;
    private boolean isChangingDirection = false;
    private final float YAW_DEADZONE = 0.1f;

    private float currentIsoScale = 15;
    private float requestedIsoScale = 15;
    private float isoSlider = 0;
    private final float ISO_DEADZONE = 0.1f;

    private float currentIsoDistance = 1;
    private float requestedIsoDistance = 1;

    private boolean isCameraIso = false;
    private boolean requestedCameraIso = false;

    protected MixinEntityPlayer(EntityType<? extends LivingEntity> entity, World world) {
        super(entity, world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo cbi) {
        IGameRenderExtension gameRenderExtension = (IGameRenderExtension) MinecraftClient.getInstance().gameRenderer;
        gameRenderExtension.addOnRenderEventHandler(e -> {
            if (!world.isClient())
                return;

            if (requestedCameraIso != isCameraIso) {
                if(requestedCameraIso){
                    isoSlider = MathUtilities.lerp(isoSlider, 1, 0.2f);
                    if(Math.abs(isoSlider) < ISO_DEADZONE){
                        isoSlider = 1;
                        isCameraIso = true;
                    }
                }else{
                    isoSlider = MathUtilities.lerp(isoSlider, 0, 0.2f);
                    if(Math.abs(isoSlider) < ISO_DEADZONE){
                        isoSlider = 0;
                        isCameraIso = false;
                    }
                }
            }

            if (isCameraIso) {
                currentIsoScale = requestedIsoScale;
                currentIsoDistance = requestedIsoDistance;

                this.pitch = 0;

                if (requestedDirection == CameraDirection.NORTH && currentDirection != CameraDirection.NORTH) {
                    isChangingDirection = true;
                    this.yaw = MathUtilities.angleLerp(this.yaw, 0, 0.2f);
                    if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 0)) < YAW_DEADZONE) {
                        this.yaw = 0;
                        currentDirection = CameraDirection.NORTH;
                        isChangingDirection = false;
                    }
                }
                if (requestedDirection == CameraDirection.SOUTH && currentDirection != CameraDirection.SOUTH) {
                    isChangingDirection = true;
                    this.yaw = MathUtilities.angleLerp(this.yaw, 180, 0.2f);
                    if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 180)) < YAW_DEADZONE) {
                        this.yaw = 180;
                        currentDirection = CameraDirection.SOUTH;
                        isChangingDirection = false;
                    }
                }
                if (requestedDirection == CameraDirection.EAST && currentDirection != CameraDirection.EAST) {
                    isChangingDirection = true;
                    this.yaw = MathUtilities.angleLerp(this.yaw, 90, 0.2f);
                    if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 90)) < YAW_DEADZONE) {
                        this.yaw = 90;
                        currentDirection = CameraDirection.EAST;
                        isChangingDirection = false;
                    }
                }
                if (requestedDirection == CameraDirection.WEST && currentDirection != CameraDirection.WEST) {
                    isChangingDirection = true;
                    this.yaw = MathUtilities.angleLerp(this.yaw, 270, 0.2f);
                    if (Math.abs(MathUtilities.shortAngleDist(this.yaw, 270)) < YAW_DEADZONE) {
                        this.yaw = 270;
                        currentDirection = CameraDirection.WEST;
                        isChangingDirection = false;
                    }
                }
            }
        });
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
    public float getIsoDistance() {
        return currentIsoDistance;
    }

    @Override
    public float getIsoSlider(){ return this.isoSlider; }

    @Override
    public void setIsoDistance(float requestedIsoDistance) {
        this.requestedIsoDistance = requestedIsoDistance;
    }

    @Override
    public boolean isChangingDirection() {
        return isChangingDirection;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        ticks++;
    }

    @Override
    public void setCameraDirection(CameraDirection direction) {
        this.requestedDirection = direction;
    }

    @Override
    public CameraDirection getCameraDirection() {
        return currentDirection;
    }

    @Override
    public void rotateCameraLeft() {
        if (getCameraDirection() == CameraDirection.NORTH) {
            setCameraDirection(CameraDirection.WEST);
        } else if (getCameraDirection() == CameraDirection.WEST) {
            setCameraDirection(CameraDirection.SOUTH);
        } else if (getCameraDirection() == CameraDirection.SOUTH) {
            setCameraDirection(CameraDirection.EAST);
        } else if (getCameraDirection() == CameraDirection.EAST) {
            setCameraDirection(CameraDirection.NORTH);
        }
        System.out.println(getCameraDirection().name());
    }

    @Override
    public void rotateCameraRight() {
        if (getCameraDirection() == CameraDirection.NORTH) {
            setCameraDirection(CameraDirection.EAST);
        } else if (getCameraDirection() == CameraDirection.EAST) {
            setCameraDirection(CameraDirection.SOUTH);
        } else if (getCameraDirection() == CameraDirection.SOUTH) {
            setCameraDirection(CameraDirection.WEST);
        } else if (getCameraDirection() == CameraDirection.WEST) {
            setCameraDirection(CameraDirection.NORTH);
        }
        System.out.println(getCameraDirection().name());
    }

    @Override
    public boolean isCameraIso() {
        return requestedCameraIso;
    }

    @Override
    public void setCameraIso(boolean state) {
        requestedCameraIso = state;
    }
}