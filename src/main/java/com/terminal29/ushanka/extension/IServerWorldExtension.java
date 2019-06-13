package com.terminal29.ushanka.extension;

import java.util.function.Consumer;

public interface IServerWorldExtension {
    void addOnTickAction(Consumer<Boolean> action);
}
