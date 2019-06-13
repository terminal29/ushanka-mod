package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.extension.IServerWorldExtension;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements IServerWorldExtension {
    private ConcurrentLinkedQueue<Consumer<Boolean>> onTickActions = new ConcurrentLinkedQueue<>();

    @Override
    public void addOnTickAction(Consumer<Boolean> action){
        onTickActions.add(action);
    }

    @Inject(method="tick", at=@At("RETURN"))
    public void onTick(BooleanSupplier supplier, CallbackInfo info){
        while(onTickActions.size() > 0){
            Consumer<Boolean> consumer = onTickActions.poll();
            consumer.accept(true);
        }
    }
}
