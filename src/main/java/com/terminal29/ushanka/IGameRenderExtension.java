package com.terminal29.ushanka;

import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public interface IGameRenderExtension {
    void addOnRenderEventHandler(Consumer<MinecraftClient> eventHandler);
}
