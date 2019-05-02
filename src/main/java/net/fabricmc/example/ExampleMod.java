package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer {
    private FabricKeyBinding isoScaleUp, isoScaleDown, lockCamera;
	@Override
	public void onInitialize() {
        isoScaleUp = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Keybinds.ISO_SCALE_UP),
                InputUtil.Type.SCANCODE,
                GLFW.GLFW_KEY_0,
                ModInfo.DISPLAY_NAME
        ).build();

        isoScaleDown = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Keybinds.ISO_SCALE_DOWN),
                InputUtil.Type.SCANCODE,
                GLFW.GLFW_KEY_1,
                ModInfo.DISPLAY_NAME
        ).build();

        lockCamera = FabricKeyBinding.Builder.create(
                new Identifier(ModInfo.DISPLAY_NAME, ModInfo.Keybinds.LOCK_CAMERA),
                InputUtil.Type.SCANCODE,
                GLFW.GLFW_KEY_2,
                ModInfo.DISPLAY_NAME
        ).build();


        KeyBindingRegistry.INSTANCE.register(isoScaleUp);
        KeyBindingRegistry.INSTANCE.register(isoScaleDown);
        KeyBindingRegistry.INSTANCE.register(lockCamera);

        ClientTickCallback.EVENT.register(e ->
        {
            boolean isPressed = false;
            if(isoScaleUp.isPressed()){
                MixinHelpers.isoScale += 0.2f;
            }
            if(isoScaleDown.isPressed()){
                MixinHelpers.isoScale -= 0.2f;
            }
            if(lockCamera.isPressed()){
                MixinHelpers.setPlayerRotation(45,-30,0);
            }

            if(MixinHelpers.isoScale < 0.01f){
                MixinHelpers.isoScale = 0.01f;
            }
        });
	}
}
