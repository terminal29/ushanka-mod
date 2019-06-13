package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.extension.IStructureManagerExtension;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class MixinStructureManager implements IStructureManagerExtension {

    @Inject(method="getResource", at=@At(value="HEAD"), cancellable = true)
    public void onGetResource(Identifier identifier, CallbackInfoReturnable<Resource> cbi){
        if(identifier.getNamespace().compareTo(ModInfo.DISPLAY_NAME) == 0){
            if(identifier.getPath().startsWith("structures/")){
                System.out.println("Loading an ushanka structure resource from " + identifier.toString());

                Resource r = VillageIslandManager.getOrLoadStructure(identifier.getPath().substring(11));
                cbi.setReturnValue(r);
            }
        }
    }


}
