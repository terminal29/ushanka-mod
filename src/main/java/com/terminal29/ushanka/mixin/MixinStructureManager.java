package com.terminal29.ushanka.mixin;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.extension.IStructureManagerExtension;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.launch.FabricTweaker;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class MixinStructureManager implements IStructureManagerExtension {
/*
    @Inject(method="loadStructureFromFile", at=@At(value="HEAD"), cancellable = true)
    public void loadStructureFromFile(Identifier identifier_1, CallbackInfoReturnable<Structure> cbi){
        if(identifier_1.getNamespace().compareTo(ModInfo.DISPLAY_NAME) == 0){
            cbi.cancel();



        }
    }*/

    @Shadow
    @Final
    Map<String, NamespaceResourceManager> namespaceManagers;

    @Shadow
    @Final
    Set<String> namespaces;



    @Inject(method="getResource", at=@At(value="HEAD"), cancellable = true)
    public void getResource(Identifier identifier, CallbackInfoReturnable<Resource> cbi){
        if(identifier.getNamespace().compareTo(ModInfo.DISPLAY_NAME) == 0){
            if(identifier.getPath().startsWith("structures/")){
                System.out.println("Loading an ushanka structure resource from " + identifier.toString());

                Resource r = VillageIslandManager.getOrLoadStructure(identifier.getPath().substring(11));
                cbi.setReturnValue(r);
            }
        }
    }

    /*
    @Inject(
            method="loadStructureFromResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/structure/Structure;",
            at=@At(value="NEW", shift = At.Shift.AFTER, target="net/minecraft/util/Identifier"),
    locals = LocalCapture.CAPTURE_FAILHARD)
    void asdf( Identifier identifier, CallbackInfoReturnable<Identifier> callbackInfoReturnable){
        if(identifier.getNamespace().compareTo(ModInfo.DISPLAY_NAME) == 0){
            // We are loading an ushanka structure
            System.out.println("Loading Ushanka Structure file");
            callbackInfoReturnable.setReturnValue(new Identifier(ModInfo.DISPLAY_NAME, "structures/island_0.nbt"));
        }
    }*/

}
