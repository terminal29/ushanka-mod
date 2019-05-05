package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.IPlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public class MixinMouse {

@Redirect(method="updateMouse", at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    public void redirect_updateMouse_changeLookDirection(ClientPlayerEntity clientPlayerEntity, double x, double y){
        if(MinecraftClient.getInstance().player != null) {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension)MinecraftClient.getInstance().player;
            if(playerEntityExtension.isCameraIso()) {
                return; // no movement
            }
        }
    clientPlayerEntity.changeLookDirection(x,y);
    }

}
