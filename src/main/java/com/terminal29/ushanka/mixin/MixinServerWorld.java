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
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements IServerWorldExtension {
    private ConcurrentLinkedQueue<Supplier<Boolean>> onTickActions = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Supplier<Boolean>> failedTickActions = new ConcurrentLinkedQueue<>();

    @Override
    public void addOnTickAction(Supplier<Boolean> action){
        onTickActions.add(action);
    }

    @Inject(method="tick", at=@At("HEAD"))
    public void onTick(BooleanSupplier supplier, CallbackInfo info){
        // Add all previously failed tick actions and clear
        onTickActions.addAll(failedTickActions);
        failedTickActions.clear();
        // Perform all queued tick actions, if any have failed, post them to the next tick
        while(onTickActions.size() > 0){
            Supplier<Boolean> action = onTickActions.poll();
            if(!action.get()){
                failedTickActions.add(action);
            }
        }
    }
}
