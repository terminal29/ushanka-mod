package com.terminal29.ushanka.keybind;

import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class UshankaKeybinds {

    public static ExtendedKeyBinding ISO_CAMERA_LEFT;
    public static ExtendedKeyBinding ISO_CAMERA_RIGHT;
    public static ExtendedKeyBinding DEBUG_ISO_CAMERA_TOGGLE;

    public static void init() {
        KeyBindingRegistry.INSTANCE.addCategory(ModInfo.KEYBIND_CATEGORY);

        DEBUG_ISO_CAMERA_TOGGLE = new ExtendedKeyBinding(
                ModInfo.identifierFor(ModInfo.KEYBIND_ISO_CAMERA_TOGGLE),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.o").getKeyCode(),
                ModInfo.KEYBIND_CATEGORY
        );

        ISO_CAMERA_LEFT = new ExtendedKeyBinding(
                ModInfo.identifierFor(ModInfo.KEYBIND_ISO_CAMERA_LEFT),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.j").getKeyCode(),
                ModInfo.KEYBIND_CATEGORY
        );

        ISO_CAMERA_RIGHT = new ExtendedKeyBinding(
                ModInfo.identifierFor(ModInfo.KEYBIND_ISO_CAMERA_RIGHT),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.l").getKeyCode(),
                ModInfo.KEYBIND_CATEGORY
        );

        DEBUG_ISO_CAMERA_TOGGLE.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension != null) {
                playerEntityExtension.requestCameraIso(!playerEntityExtension.isCameraIso());
            }
        });

        ISO_CAMERA_LEFT.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension != null && playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraLeft();
            }
        });

        ISO_CAMERA_RIGHT.addOnPressedHandler(keyBinding -> {
            IClientPlayerEntityExtension playerEntityExtension = (IClientPlayerEntityExtension) MinecraftClient.getInstance().player;
            if (playerEntityExtension != null && playerEntityExtension.isCameraIso() && !playerEntityExtension.isChangingDirection()) {
                playerEntityExtension.rotateCameraRight();
            }
        });
    }
}
