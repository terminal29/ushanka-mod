package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity extends Object{
/*
    @Inject(method="toTag", at=@At("RETURN"))
    public CompoundTag toTag(CompoundTag compoundTag_1, CallbackInfoReturnable cbi) {
        if((Object)this instanceof ServerPlayerEntity){
            IServerPlayerEntityExtension $this = (IServerPlayerEntityExtension)this;
            compoundTag_1.put(ModInfo.DISPLAY_NAME, $this.serialize());
        }
        return null;
    }

    @Inject(method="fromTag", at=@At("RETURN"))
    public void fromTag(CompoundTag compoundTag_1, CallbackInfo cbi) {
        if((Object)this instanceof ServerPlayerEntity){
            IServerPlayerEntityExtension $this = (IServerPlayerEntityExtension)this;
            if(compoundTag_1.containsKey(ModInfo.DISPLAY_NAME)){
                $this.deserialize(compoundTag_1.getCompound(ModInfo.DISPLAY_NAME));
            }
        }
    }*/
}
