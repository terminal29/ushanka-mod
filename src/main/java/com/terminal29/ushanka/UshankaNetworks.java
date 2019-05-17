package com.terminal29.ushanka;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class UshankaNetworks {
    public static void init(){
        ServerSidePacketRegistry.INSTANCE.register(new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Packets.CHANGE_DIMENSION), (context, buffer) -> {
            boolean toVillage = buffer.readBoolean();
            if(toVillage) {
                System.out.println("Teleporting " + context.getPlayer().getName().getText() + " to the village...");
                ((IPlayerEntityExtension) context.getPlayer()).teleportToVillage();
            }else{
                System.out.println("Teleporting " + context.getPlayer().getName().getText() + " to previous world...");
                ((IPlayerEntityExtension) context.getPlayer()).teleportToOverworld();
            }
        });
    }
}
