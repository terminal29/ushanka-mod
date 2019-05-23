package com.terminal29.ushanka;

import com.terminal29.ushanka.dimension.UshankaDimensions;
import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.PersistentState;
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
        return server.getWorld(UshankaDimensions.VILLAGE).getPersistentStateManager().getOrCreate(UshankaPersistentData::new, ModInfo.DISPLAY_NAME);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ushankaPlayerStorage = new CompoundTag();
        System.out.println("@fromTag");
        System.out.println(tag);

        for(String playerUUID : tag.getKeys()){
            ushankaPlayerStorage.put(playerUUID, tag.getCompound(playerUUID));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        System.out.println("@toTag");
        System.out.println(tag);

        for(String playerUUID : ushankaPlayerStorage.getKeys()){
            ushankaPlayerStorage.put(playerUUID, tag.getCompound(playerUUID));
        }
        return tag;
    }

    private void setPlayerTag(UUID playerUUID){
        if(!ushankaPlayerStorage.getKeys().contains(playerUUID.toString())){
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(ModInfo.Packets.ISO_STATE, false);
            tag.putString(ModInfo.Packets.ISO_DIRECTION, IClientPlayerEntityExtension.CameraDirection.NONE.name());
            ushankaPlayerStorage.put(playerUUID.toString(), tag);
        }
    }

    public void setPlayerIsoState(UUID playerUUID, boolean state){
        setPlayerTag(playerUUID);
        ushankaPlayerStorage.getCompound(playerUUID.toString()).putBoolean(ModInfo.Packets.ISO_STATE, state);
    }

    public boolean getPlayerIsoState(UUID playerUUID){
        setPlayerTag(playerUUID);
        return ushankaPlayerStorage.getCompound(playerUUID.toString()).getBoolean(ModInfo.Packets.ISO_STATE);
    }

    public void setPlayerIsoDirection(UUID playerUUID, IClientPlayerEntityExtension.CameraDirection direction){
        setPlayerTag(playerUUID);
        ushankaPlayerStorage.getCompound(playerUUID.toString()).putString(ModInfo.Packets.ISO_DIRECTION, direction.name());
    }

    public IClientPlayerEntityExtension.CameraDirection getPlayerIsoDirection(UUID playerUUID){
        setPlayerTag(playerUUID);
        return IClientPlayerEntityExtension.CameraDirection.fromName(ushankaPlayerStorage.getCompound(playerUUID.toString()).getString(ModInfo.Packets.ISO_DIRECTION));
    }


}