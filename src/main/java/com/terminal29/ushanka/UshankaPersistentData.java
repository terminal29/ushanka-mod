package com.terminal29.ushanka;

import com.terminal29.ushanka.utility.IsoCameraDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;

import java.util.UUID;

public class UshankaPersistentData extends PersistentState {

    private CompoundTag ushankaPlayerStorage = new CompoundTag();

    /**
     * {
     *     "uuidString":{
     *         "is_camera_iso":{boolean}
     *         "camera_direction":{string}
     *     }
     * }
     */

    public UshankaPersistentData() {
        super(ModInfo.DISPLAY_NAME);
    }

    public static UshankaPersistentData get(MinecraftServer server){
        return server.getWorld(DimensionType.OVERWORLD).getPersistentStateManager().getOrCreate(UshankaPersistentData::new, ModInfo.DISPLAY_NAME);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ushankaPlayerStorage = new CompoundTag();
        for(String playerUUID : tag.getKeys()){
            ushankaPlayerStorage.put(playerUUID, tag.getCompound(playerUUID));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        for(String playerUUID : ushankaPlayerStorage.getKeys()){
            tag.put(playerUUID, ushankaPlayerStorage.getCompound(playerUUID));
        }
        return tag;
    }

    private void setPlayerTag(UUID playerUUID){
        if(!ushankaPlayerStorage.getKeys().contains(playerUUID.toString())){
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(ModInfo.PACKET_ISO_STATE, false);
            tag.putString(ModInfo.PACKET_ISO_DIRECTION, IsoCameraDirection.NONE.name());
            ushankaPlayerStorage.put(playerUUID.toString(), tag);
        }
        markDirty();
    }

    public void setPlayerIsoState(UUID playerUUID, boolean state){
        setPlayerTag(playerUUID);
        ushankaPlayerStorage.getCompound(playerUUID.toString()).putBoolean(ModInfo.PACKET_ISO_STATE, state);
        markDirty();
    }

    public boolean getPlayerIsoState(UUID playerUUID){
        setPlayerTag(playerUUID);
        return ushankaPlayerStorage.getCompound(playerUUID.toString()).getBoolean(ModInfo.PACKET_ISO_STATE);
    }

    public void setPlayerIsoDirection(UUID playerUUID, IsoCameraDirection direction){
        setPlayerTag(playerUUID);
        ushankaPlayerStorage.getCompound(playerUUID.toString()).putString(ModInfo.PACKET_ISO_DIRECTION, direction.name());
        markDirty();
    }

    public IsoCameraDirection getPlayerIsoDirection(UUID playerUUID){
        setPlayerTag(playerUUID);
        return IsoCameraDirection.fromName(ushankaPlayerStorage.getCompound(playerUUID.toString()).getString(ModInfo.PACKET_ISO_DIRECTION));
    }


}