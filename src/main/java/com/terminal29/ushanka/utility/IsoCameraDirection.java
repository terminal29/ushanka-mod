package com.terminal29.ushanka.utility;

public enum IsoCameraDirection {
    NORTH, // 180
    SOUTH, // 0
    EAST, // 270
    WEST, // 90
    NONE; // ~

    public static IsoCameraDirection fromName(String name){
        for(IsoCameraDirection direction : IsoCameraDirection.values()){
            if(direction.name().compareToIgnoreCase(name) == 0)
                return direction;
        }
        return IsoCameraDirection.NONE;
    }
}
