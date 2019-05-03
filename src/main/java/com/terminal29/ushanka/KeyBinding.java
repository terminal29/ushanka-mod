package com.terminal29.ushanka;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KeyBinding {
    private List<Consumer<KeyBinding>> onPressedHandlers = new ArrayList<>();
    private List<Consumer<KeyBinding>> onHeldHandlers = new ArrayList<>();
    private List<Consumer<KeyBinding>> onReleasedHandlers = new ArrayList<>();

    private FabricKeyBinding keyBind;
    private boolean wasPressed = false;
    private boolean isPressed = false;

    public KeyBinding(FabricKeyBinding keyBind) {
        this.keyBind = keyBind;
        KeyBindingRegistry.INSTANCE.register(keyBind);
        ClientTickCallback.EVENT.register(e -> {
            if (this.keyBind.isPressed()) {
                if (isPressed) {
                    onHeld();
                    wasPressed = true;
                }else{
                    onPressed();
                }
                isPressed = true;
            } else {
                if (!isPressed) {
                    wasPressed = false;
                }else{
                    onReleased();
                }
                isPressed = false;
            }
        });
    }

    public void addOnPressedHandler(Consumer<KeyBinding> handler){
        onPressedHandlers.add(handler);
    }

    public void addOnHeldHandler(Consumer<KeyBinding> handler){
        onHeldHandlers.add(handler);
    }

    public void addOnReleasedHandler(Consumer<KeyBinding> handler){
        onReleasedHandlers.add(handler);
    }

    private void onPressed(){
        for (Consumer<KeyBinding> handler : onPressedHandlers) {
            handler.accept(this);
        }
    }

    private void onHeld(){
        for (Consumer<KeyBinding> handler : onHeldHandlers) {
            handler.accept(this);
        }
    }

    private void onReleased(){
        for (Consumer<KeyBinding> handler : onReleasedHandlers) {
            handler.accept(this);
        }
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
