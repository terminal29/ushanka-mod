package com.terminal29.ushanka.keybind;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class UshankaKeybinds {
    private static KeyBinding isoScaleUp, isoScaleDown, isoCameraLeft, isoCameraRight, isoCameraToggle;

    public static void init() {
        KeyBindingRegistry.INSTANCE.addCategory(ModInfo.KEYBIND_CATEGORY);
        isoScaleUp = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.KEYBIND_CATEGORY, ModInfo.KEYBIND_ISO_SCALE_UP),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.i").getKeyCode(),
                        ModInfo.KEYBIND_CATEGORY
                ).build()
        );

        isoScaleDown = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.KEYBIND_CATEGORY, ModInfo.KEYBIND_ISO_SCALE_DOWN),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.k").getKeyCode(),
                        ModInfo.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraLeft = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.KEYBIND_CATEGORY, ModInfo.KEYBIND_ISO_CAMERA_LEFT),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.j").getKeyCode(),
                        ModInfo.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraRight = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.KEYBIND_CATEGORY, ModInfo.KEYBIND_ISO_CAMERA_RIGHT),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.l").getKeyCode(),
                        ModInfo.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraToggle = new KeyBinding(
                FabricKeyBinding.Builder.create(
                        new Identifier(ModInfo.KEYBIND_CATEGORY, ModInfo.KEYBIND_ISO_CAMERA_TOGGLE),
                        InputUtil.Type.KEYSYM,
                        InputUtil.fromName("key.keyboard.o").getKeyCode(),
                        ModInfo.KEYBIND_CATEGORY
                ).build()
        );

        isoCameraToggle.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension != null) {
                playerEntityExtension.requestCameraIso(!playerEntityExtension.isCameraIso());
            }
        });

        isoCameraLeft.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraLeft();
            }
        });

        isoCameraRight.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraRight();
            }
        });

        isoScaleUp.addOnHeldHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso()) {
                playerEntityExtension.setIsoDistance(playerEntityExtension.getIsoDistance() + 0.2f);
            }
        });

        isoScaleDown.addOnHeldHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension.isCameraIso()) {
                playerEntityExtension.setIsoDistance(playerEntityExtension.getIsoDistance() - 0.2f);
            }
        });
    }
}
