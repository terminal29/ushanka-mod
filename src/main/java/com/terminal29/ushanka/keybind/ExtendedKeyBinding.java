package com.terminal29.ushanka.keybind;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExtendedKeyBinding extends FabricKeyBinding {
    private List<Consumer<ExtendedKeyBinding>> onPressedHandlers = new ArrayList<>();
    private List<Consumer<ExtendedKeyBinding>> onHeldHandlers = new ArrayList<>();
    private List<Consumer<ExtendedKeyBinding>> onReleasedHandlers = new ArrayList<>();

    private boolean wasPressed = false;
    private boolean isPressed = false;


    public ExtendedKeyBinding(Identifier id, InputUtil.Type type, int code, String category) {
        super(id, type, code, category);
        KeyBindingRegistry.INSTANCE.register(this);
        ClientTickCallback.EVENT.register(e -> {
            if (super.isPressed()) {
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

    public void addOnPressedHandler(Consumer<ExtendedKeyBinding> handler){
        onPressedHandlers.add(handler);
    }

    public void addOnHeldHandler(Consumer<ExtendedKeyBinding> handler){
        onHeldHandlers.add(handler);
    }

    public void addOnReleasedHandler(Consumer<ExtendedKeyBinding> handler){
        onReleasedHandlers.add(handler);
    }

    private void onPressed(){
        for (Consumer<ExtendedKeyBinding> handler : onPressedHandlers) {
            handler.accept(this);
        }
    }

    private void onHeld(){
        for (Consumer<ExtendedKeyBinding> handler : onHeldHandlers) {
            handler.accept(this);
        }
    }

    private void onReleased(){
        for (Consumer<ExtendedKeyBinding> handler : onReleasedHandlers) {
            handler.accept(this);
        }
    }

    @Override
    public boolean wasPressed() {
        return wasPressed;
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }

}
