package com.terminal29.ushanka;

import net.minecraft.util.Identifier;

public class ModInfo {
    public static final String DISPLAY_NAME = "ushanka";
    public class Dimensions {
        public static final String DIMENSION_NAME = "village";
        public static final String BIOME = "village";
    }

    public static Identifier identifierFor(String object){
        return new Identifier(ModInfo.DISPLAY_NAME, object);
    }

    public class Keybinds {
        public static final String KEYBIND_CATEGORY = "controls." + ModInfo.DISPLAY_NAME;
        public static final String ISO_SCALE_UP = "iso_scale_up";
        public static final String ISO_SCALE_DOWN = "iso_scale_down";
        public static final String ISO_CAMERA_LEFT = "iso_camera_left";
        public static final String ISO_CAMERA_RIGHT = "iso_camera_right";
        public static final String ISO_CAMERA_TOGGLE = "iso_camera_toggle";
    }

    public class Items {
        public static final String USHANKA = "ushanka";
    }

    public class Packets {
        public static final String CHANGE_DIMENSION = "change_dimension";
        public static final String ISO_STATE = "iso_state";
        public static final String ISO_DIRECTION = "iso_direction";
        public static final String CLIENT_READY = "client_ready";
    }
}
