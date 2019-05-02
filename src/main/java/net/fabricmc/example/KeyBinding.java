package net.fabricmc.example;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class KeyBinding {
    private FabricKeyBinding keyBind;
    private boolean wasPressed = false;
    private boolean isPressed = false;

    public KeyBinding(FabricKeyBinding keyBind) {
        this.keyBind = keyBind;
        KeyBindingRegistry.INSTANCE.register(keyBind);
        ClientTickCallback.EVENT.register(e -> {
            if (this.keyBind.isPressed()) {
                if (isPressed) {
                    wasPressed = true;
                }
                isPressed = true;
            } else {
                if (!isPressed) {
                    wasPressed = false;
                }
                isPressed = false;
            }
        });
    }

    boolean wasPressed() {
        return wasPressed;
    }

    boolean isPressed() {
        return isPressed;
    }

    FabricKeyBinding getKeyBind() {
        return keyBind;
    }

}
