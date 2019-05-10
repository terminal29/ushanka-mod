package com.terminal29.ushanka;

import com.terminal29.ushanka.dimension.DimensionVillage;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.item.UshankaItems;
import com.terminal29.ushanka.keybind.KeyBinding;
import com.terminal29.ushanka.keybind.UshankaKeybinds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Ushanka implements ModInitializer {
    @Override
    public void onInitialize() {
        UshankaKeybinds.init();
        UshankaDimensions.init();
        UshankaItems.init();
    }
}
