package net.fabricmc.example.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinEntityPlayer extends LivingEntity{

    long ticks = 0;

    protected MixinEntityPlayer(EntityType<? extends LivingEntity> entity, World world) {
        super(entity, world);
    }

    @Inject(method="tick", at=@At("HEAD"))
    protected void onTick(CallbackInfo info){

    }
}