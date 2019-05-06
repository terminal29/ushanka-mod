package com.terminal29.ushanka;

import com.terminal29.ushanka.dimension.DimensionTypeVillage;
import com.terminal29.ushanka.dimension.DimensionVillage;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class Ushanka implements ModInitializer {
    private KeyBinding isoScaleUp, isoScaleDown, isoCameraLeft, isoCameraRight, isoCameraToggle;

    @Override
    public void onInitialize() {
        setupKeybinds();
        setupWorld();
    }

    private void setupWorld(){
        DimensionVillage.initStatic();
    }

    private void setupKeybinds(){
        KeyBindingRegistry.INSTANCE.addCategory(ModInfo.Keybinds.KEYBIND_CATEGORY);
        isoScaleUp = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_SCALE_UP),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.i").getKeyCode(),
                        ModInfo.Keybinds.KEYBIND_CATEGORY
                ).build()
        );

        isoScaleDown = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_SCALE_DOWN),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.k").getKeyCode(),
                        ModInfo.Keybinds.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraLeft = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_CAMERA_LEFT),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.j").getKeyCode(),
                        ModInfo.Keybinds.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraRight = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_CAMERA_RIGHT),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.l").getKeyCode(),
                        ModInfo.Keybinds.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraToggle = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_CAMERA_TOGGLE),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.o").getKeyCode(),
                        ModInfo.Keybinds.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraToggle.addOnPressedHandler(keyBinding -> {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension != null) {
                playerEntityExtension.setCameraIso(!playerEntityExtension.isCameraIso());
            }
        });

        isoCameraLeft.addOnPressedHandler(keyBinding -> {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraLeft();
            }
        });

        isoCameraRight.addOnPressedHandler(keyBinding -> {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraRight();
            }
        });

        isoScaleUp.addOnHeldHandler(keyBinding -> {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso()) {
                playerEntityExtension.setIsoScale(playerEntityExtension.getIsoScale() + 0.2f);
            }
        });

        isoScaleDown.addOnHeldHandler(keyBinding -> {
            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso()) {
                playerEntityExtension.setIsoScale(playerEntityExtension.getIsoScale() - 0.2f);
            }
            if (playerEntityExtension.getIsoScale() < 0.01f) {
                playerEntityExtension.setIsoScale(0.01f);
            }
        });
    }

    public static void DrawDebugText(String text, int x, int y, int size) {
        if (MinecraftClient.getInstance().textRenderer != null && MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.drawString(MinecraftClient.getInstance().textRenderer, text, x, y, size);
        }
    }
}
