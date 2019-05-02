package net.fabricmc.example.mixin;

import net.fabricmc.example.IPlayerEntityExtension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fabricmc.example.IPlayerEntityExtension.CameraDirection.*;

@Mixin(PlayerEntity.class)
public abstract class MixinEntityPlayer extends LivingEntity implements IPlayerEntityExtension {

    private long ticks = 0;
    private CameraDirection direction = CameraDirection.NORTH;

    private float isoScale = 25;
    private float isoDistance = 1;

    protected MixinEntityPlayer(EntityType<? extends LivingEntity> entity, World world) {
        super(entity, world);
    }

    public float getIsoScale(){
        return isoScale;
    }

    public void setIsoScale(float isoScale){
        this.isoScale = isoScale;
    }

    public float getIsoDistance(){
        return isoDistance;
    }

    public void setIsoDistance(float isoDistance){
        this.isoDistance = isoDistance;
    }

    @Inject(method="tick", at=@At("HEAD"))
    protected void onTick(CallbackInfo info){
        ticks++;
    }

    @Override
    public void setCameraDirection(CameraDirection direction){
        this.direction = direction;
    }

    @Override
    public CameraDirection getCameraDirection(){
        return direction;
    }

    @Override
    public void rotateCameraLeft() {
        if(direction == NORTH){
            direction = WEST;
        }else if(direction == WEST){
            direction = SOUTH;
        }else if(direction == SOUTH){
            direction = EAST;
        }else if(direction == EAST){
            direction = NORTH;
        }
        System.out.println(direction.name());
    }

    @Override
    public void rotateCameraRight() {
        if(direction == NORTH){
            direction = EAST;
        }else if(direction == EAST){
            direction = SOUTH;
        }else if(direction == SOUTH){
            direction = WEST;
        }else if(direction == WEST){
            direction = NORTH;
        }
        System.out.println(direction.name());
    }
}