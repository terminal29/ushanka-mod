package com.terminal29.ushanka.mixin;

import com.mojang.authlib.GameProfile;
import com.terminal29.ushanka.dimension.DimensionVillage;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity{

    long ticks = 0;

    @Shadow
    ServerPlayNetworkHandler networkHandler;

    public MixinServerPlayerEntity(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    private boolean isCameraIso(){
        return false;// networkHandler // ask client if their camera is iso;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo info) {
        ticks++;
        System.out.println(isCameraIso() + ":" + this.dimension.getRawId());
        if(this.dimension != DimensionVillage.VILLAGE)
            changeDimension(DimensionVillage.VILLAGE);

    }
}
