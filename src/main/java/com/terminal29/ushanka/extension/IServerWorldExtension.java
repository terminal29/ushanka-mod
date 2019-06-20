package com.terminal29.ushanka.extension;

import java.util.function.Supplier;

public interface IServerWorldExtension {
    void addOnTickAction(Supplier<Boolean> action);
}
