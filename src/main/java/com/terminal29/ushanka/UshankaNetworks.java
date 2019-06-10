package com.terminal29.ushanka;

import com.terminal29.ushanka.extension.IClientPlayerEntityExtension;
import com.terminal29.ushanka.extension.IPlayerEntityExtension;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

public class UshankaNetworks {
    public static void init(){

        // Tells server to update a players iso state
        ServerSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_ISO_STATE), (context, buffer) -> {
            boolean state = buffer.readBoolean();
            System.out.println(ModInfo.PACKET_ISO_STATE + " " + state);
            ((IPlayerEntityExtension)context.getPlayer()).onCameraIsoChanged(state, false);
        });

        // Tells client to update a players iso state
        ClientSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_ISO_STATE), (context, buffer) -> {
            boolean state = buffer.readBoolean();
            System.out.println(ModInfo.PACKET_ISO_STATE + " " + state);
            ((IPlayerEntityExtension)context.getPlayer()).onCameraIsoChanged(state, false);
        });

        // Tells server to update a players iso direction
        ServerSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_ISO_DIRECTION), (context, buffer) -> {
            String direction = buffer.readString();
            System.out.println(ModInfo.PACKET_ISO_DIRECTION + " " + direction);
            ((IPlayerEntityExtension)context.getPlayer()).onCameraDirectionChanged(IClientPlayerEntityExtension.CameraDirection.fromName(direction), false);
        });

        // Tells a client to update a players iso direction
        ClientSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_ISO_DIRECTION), (context, buffer) -> {
            String direction = buffer.readString();
            System.out.println(ModInfo.PACKET_ISO_DIRECTION + " " + direction);
            ((IPlayerEntityExtension)context.getPlayer()).onCameraDirectionChanged(IClientPlayerEntityExtension.CameraDirection.fromName(direction), false);
        });

        // Tells client to teleport to a dimension
        ClientSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_CHANGE_DIMENSION), (context, buffer) -> {
            DimensionType dimension = DimensionType.byId(buffer.readIdentifier());
            System.out.println(ModInfo.PACKET_CHANGE_DIMENSION);
            ((IPlayerEntityExtension)context.getPlayer()).changeToDimension(dimension);
        });

        // Client tells server it is ready to recieve init info
        ServerSidePacketRegistry.INSTANCE.register(ModInfo.identifierFor(ModInfo.PACKET_CLIENT_READY), (context, buffer) -> {
            System.out.println(ModInfo.PACKET_CLIENT_READY);
            ((IServerPlayerEntityExtension)context.getPlayer()).onClientReady();
        });
    }
}
