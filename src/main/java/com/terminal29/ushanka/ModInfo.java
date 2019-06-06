package com.terminal29.ushanka;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ModInfo {
    public static final String DISPLAY_NAME = "ushanka";

    // Dimensions
    public static final String DIMENSION_NAME = "village";
    public static final String DIMENSION_BIOME = "village";

    //Keybinds
    public static final String KEYBIND_CATEGORY = "controls." + ModInfo.DISPLAY_NAME;
    public static final String KEYBIND_ISO_SCALE_UP = "iso_scale_up";
    public static final String KEYBIND_ISO_SCALE_DOWN = "iso_scale_down";
    public static final String KEYBIND_ISO_CAMERA_LEFT = "iso_camera_left";
    public static final String KEYBIND_ISO_CAMERA_RIGHT = "iso_camera_right";
    public static final String KEYBIND_ISO_CAMERA_TOGGLE = "iso_camera_toggle";

    //Items
    public static final String ITEM_USHANKA = "ushanka";

    // Packets
    public static final String PACKET_CHANGE_DIMENSION = "change_dimension";
    public static final String PACKET_ISO_STATE = "iso_state";
    public static final String PACKET_ISO_DIRECTION = "iso_direction";
    public static final String PACKET_CLIENT_READY = "client_ready";

    // Islands
    public static final String ISLAND_STRUCTURE_0 = "island_0";

    // Blocks
    public static final String BLOCK_BLUE_STONE = "blue_stone";
    public static final String BLOCK_BLUE_STONE_BRICK = "blue_stone_brick";
    public static final String BLOCK_BLUE_STONE_SQUARE_BRICK = "blue_stone_square_brick";
    public static final String BLOCK_GRASSY_BLUE_STONE = "grassy_blue_stone";
    public static final String BLOCK_GRASSY_BLUE_STONE_BRICK = "grassy_blue_stone_brick";
    public static final String BLOCK_GRASSY_BLUE_STONE_SQUARE_BRICK = "grassy_blue_stone_square_brick";
    public static final String BLOCK_BROWN_STONE = "brown_stone";
    public static final String BLOCK_BROWN_STONE_INNER_SQUARE = "brown_stone_inner_square";
    public static final String BLOCK_BROWN_STONE_OUTER_SQUARE = "brown_stone_outer_square";
    public static final String BLOCK_GRASSY_BROWN_STONE = "grassy_brown_stone";
    public static final String BLOCK_GRASSY_BROWN_STONE_INNER_SQUARE = "grassy_brown_stone_inner_square";
    public static final String BLOCK_GRASSY_BROWN_STONE_OUTER_SQUARE = "grassy_brown_stone_outer_square";



    private static List<String> islandList = Arrays.asList(
            ISLAND_STRUCTURE_0
    );

    private final String theName;
    ModInfo(String theName){
        this.theName = theName;
    }

    @Override
    public String toString(){
        return this.theName;
    }

    public static Identifier identifierFor(String object){
        return new Identifier(ModInfo.DISPLAY_NAME, object);
    }

    public static int numIslandStructures(){
        return islandList.size();
    }

    public static Identifier getStructureIdentifier(int id){
        return identifierFor(islandList.get(id));
    }
}

