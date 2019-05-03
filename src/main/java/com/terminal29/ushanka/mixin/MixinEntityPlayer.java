package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.IGameRenderExtension;
import com.terminal29.ushanka.IPlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinEntityPlayer extends LivingEntity implements IPlayerEntityExtension {

    private long ticks = 0;
    private CameraDirection direction = CameraDirection.NORTH;

    private float isoScale = 15;
    private float isoDistance = 1;

    private boolean isCameraIso = false;

    protected MixinEntityPlayer(EntityType<? extends LivingEntity> entity, World world) {
        super(entity, world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo cbi) {
        ((IGameRenderExtension) MinecraftClient.getInstance().gameRenderer).AddOnRenderEventHandler(e -> {
            if(isCameraIso){
                this.pitch = 0;
                if (direction == CameraDirection.NORTH) {
                    this.yaw = angleLerp(this.yaw, 0, 0.2f);
                }
                if (direction == CameraDirection.SOUTH) {
                    this.yaw = angleLerp(this.yaw, 180, 0.2f);
                }
                if (direction == CameraDirection.EAST) {
                    this.yaw = angleLerp(this.yaw, 90, 0.2f);
                }
                if (direction == CameraDirection.WEST) {
                    this.yaw = angleLerp(this.yaw, -90, 0.2f);
                }
            }
        });
    }

    @Override
    public float getIsoScale() {
        return isoScale;
    }

    @Override
    public void setIsoScale(float isoScale) {
        this.isoScale = isoScale;
    }

    @Override
    public float getIsoDistance() {
        return isoDistance;
    }

    @Override
    public void setIsoDistance(float isoDistance) {
        this.isoDistance = isoDistance;
    }

    // https://gist.github.com/shaunlebron/8832585
    float shortAngleDist(float a0, float a1) {
        float max = (float) 360;
        float da = (a1 - a0) % max;
        return 2 * da % max - da;
    }

    float angleLerp(float a0, float a1, float t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        ticks++;
    }

    @Override
    public void setCameraDirection(CameraDirection direction) {
        this.direction = direction;
    }

    @Override
    public CameraDirection getCameraDirection() {
        return direction;
    }

    @Override
    public void rotateCameraLeft() {
        if (direction == CameraDirection.NORTH) {
            direction = CameraDirection.WEST;
        } else if (direction == CameraDirection.WEST) {
            direction = CameraDirection.SOUTH;
        } else if (direction == CameraDirection.SOUTH) {
            direction = CameraDirection.EAST;
        } else if (direction == CameraDirection.EAST) {
            direction = CameraDirection.NORTH;
        }
        System.out.println(direction.name());
    }

    @Override
    public void rotateCameraRight() {
        if (direction == CameraDirection.NORTH) {
            direction = CameraDirection.EAST;
        } else if (direction == CameraDirection.EAST) {
            direction = CameraDirection.SOUTH;
        } else if (direction == CameraDirection.SOUTH) {
            direction = CameraDirection.WEST;
        } else if (direction == CameraDirection.WEST) {
            direction = CameraDirection.NORTH;
        }
        System.out.println(direction.name());
    }

    @Override
    public boolean isCameraIso(){
        return isCameraIso;
    }

    @Override
    public void setCameraIso(boolean state){
        isCameraIso = state;
    }
}