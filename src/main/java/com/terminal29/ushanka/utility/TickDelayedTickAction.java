package com.terminal29.ushanka.utility;

import java.util.function.Supplier;

public class TickDelayedTickAction implements Supplier<Boolean>{
    private final Supplier<Boolean> supplier;
    private final int waitTicks;
    private int currentTicks = 0;
    public TickDelayedTickAction(int waitTicks, Supplier<Boolean> supplier){
        this.supplier = supplier;
        this.waitTicks = waitTicks;
    }

    @Override
    public Boolean get() {
        if(currentTicks >= waitTicks)
            return supplier.get();
        currentTicks++;
        return false;
    }
}