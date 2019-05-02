package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class ExampleMod implements ModInitializer {
    private FabricKeyBinding isoScaleUp, isoScaleDown, isoCameraLeft, isoCameraRight;
	@Override
	public void onInitialize() {
        isoScaleUp = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_SCALE_UP),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.i").getKeyCode(),
                ModInfo.Keybinds.KEYBIND_CATEGORY
        ).build();

        isoScaleDown = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_SCALE_DOWN),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.k").getKeyCode(),
                ModInfo.Keybinds.KEYBIND_CATEGORY
        ).build();

        isoCameraLeft = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_CAMERA_LEFT),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.j").getKeyCode(),
                ModInfo.Keybinds.KEYBIND_CATEGORY
        ).build();

        isoCameraRight = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.Keybinds.KEYBIND_CATEGORY, ModInfo.Keybinds.ISO_CAMERA_RIGHT),
                InputUtil.Type.KEYSYM,
                InputUtil.fromName("key.keyboard.l").getKeyCode(),
                ModInfo.Keybinds.KEYBIND_CATEGORY
        ).build();


        KeyBindingRegistry.INSTANCE.addCategory(ModInfo.Keybinds.KEYBIND_CATEGORY);
        KeyBindingRegistry.INSTANCE.register(isoScaleUp);
        KeyBindingRegistry.INSTANCE.register(isoScaleDown);
        KeyBindingRegistry.INSTANCE.register(isoCameraLeft);
        KeyBindingRegistry.INSTANCE.register(isoCameraRight);


        ClientTickCallback.EVENT.register(e ->
        {

            IPlayerEntityExtension playerEntityExtension = (IPlayerEntityExtension) MinecraftClient.getInstance().player;
            if(playerEntityExtension != null){

                if(isoScaleUp.isPressed()){
                    playerEntityExtension.setIsoScale(playerEntityExtension.getIsoScale() + 0.2f);
                }
                if(isoScaleDown.isPressed()){
                    playerEntityExtension.setIsoScale(playerEntityExtension.getIsoScale() - 0.2f);
                }

                if(isoCameraLeft.wasPressed() && !isoCameraLeft.isPressed()) {
                    playerEntityExtension.rotateCameraLeft();
                }
                if(isoCameraRight.wasPressed() && !isoCameraRight.isPressed()){
                    playerEntityExtension.rotateCameraRight();
                }

                if(playerEntityExtension.getIsoScale() < 0.01f){
                    playerEntityExtension.setIsoScale(0.01f);
                }
            }

        });
	}
}
