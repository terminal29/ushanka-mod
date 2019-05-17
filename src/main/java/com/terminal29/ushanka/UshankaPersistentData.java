package com.terminal29.ushanka;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.UUID;

public class UshankaPersistentData extends PersistentState {


    private HashMap<UUID, Boolean> playerIsoMap = new HashMap<>();

    public UshankaPersistentData() {
        super(ModInfo.DISPLAY_NAME);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        for(String element : tag.getKeys()){
            CompoundTag playerTag = tag.getCompound(element);
            playerIsoMap.put(playerTag.getUuid("uuid"), playerTag.getBoolean("isCameraIso"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        for(UUID id : playerIsoMap.keySet()){
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUuid("uuid", id);
            uuidTag.putBoolean("isCameraIso", playerIsoMap.get(id));
            tag.put(id.toString(), uuidTag);
        }
        return tag;
    }

    public boolean isPlayerIso(UUID playerUUID) {
        if(!playerIsoMap.containsKey(playerUUID))
            playerIsoMap.put(playerUUID, false);
        return playerIsoMap.get(playerUUID);
    }

    public void setPlayerIso(UUID playerUUID, boolean isCameraIso){
        playerIsoMap.put(playerUUID, isCameraIso);
    }
}