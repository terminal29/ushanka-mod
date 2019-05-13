package com.terminal29.ushanka;

import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.item.UshankaItems;
import com.terminal29.ushanka.keybind.UshankaKeybinds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class Ushanka implements ModInitializer {
    @Override
    public void onInitialize() {
        UshankaKeybinds.init();
        UshankaDimensions.init();
        UshankaItems.init();
        ServerSidePacketRegistry.INSTANCE.register(new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Packets.CHANGE_DIMENSION), (context, buffer) -> {
            boolean toVillage = buffer.readBoolean();
            if(toVillage) {
                System.out.println("Teleporting " + context.getPlayer().getName() + " to the village...");
                ((IPlayerEntityExtension) context.getPlayer()).teleportToVillage();
            }else{
                System.out.println("Teleporting " + context.getPlayer().getName() + " to previous world...");
                ((IPlayerEntityExtension) context.getPlayer()).teleportToPreviousWorld();
            }
        });
    }
}
