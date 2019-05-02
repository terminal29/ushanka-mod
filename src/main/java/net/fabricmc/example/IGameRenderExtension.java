package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import java.util.function.Consumer;

public interface IGameRenderExtension {
    void AddOnRenderEventHandler(Consumer<MinecraftClient> eventHandler);
}
