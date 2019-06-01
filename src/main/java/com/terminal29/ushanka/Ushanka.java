package com.terminal29.ushanka;

import com.terminal29.ushanka.block.UshankaBlocks;
import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.item.UshankaItems;
import com.terminal29.ushanka.keybind.UshankaKeybinds;
import net.fabricmc.api.ModInitializer;

public class Ushanka implements ModInitializer {
    @Override
    public void onInitialize() {
        UshankaKeybinds.init();
        UshankaDimensions.init();
        UshankaItems.init();
        UshankaBlocks.init();
        UshankaNetworks.init();
    }
}
