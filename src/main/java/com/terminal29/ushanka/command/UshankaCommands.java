package com.terminal29.ushanka.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
import com.terminal29.ushanka.extension.IServerPlayerEntityExtension;
import com.terminal29.ushanka.utility.MovementUtility;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class UshankaCommands {
    public static void init(){
        CommandRegistry.INSTANCE.register(false,
            dispatcher -> dispatcher.register(
                CommandManager.literal("save_island")
                .then(CommandManager.argument("filename", StringArgumentType.word())
                .executes(UshankaCommands::onSaveIsland))
        ));
        CommandRegistry.INSTANCE.register(false,
            dispatcher -> dispatcher.register(
                CommandManager.literal("load_island")
                .then(CommandManager.argument("filename", StringArgumentType.word())
                .executes(UshankaCommands::onLoadIsland))
        ));
        CommandRegistry.INSTANCE.register(false,
            dispatcher -> dispatcher.register(
                CommandManager.literal("goto_island")
                .then(CommandManager.argument("x", IntegerArgumentType.integer())
                .then(CommandManager.argument("y", IntegerArgumentType.integer())
                .executes(UshankaCommands::onGotoIsland)
        ))));
    }

    public static int onSaveIsland(CommandContext<ServerCommandSource> context){
        ChunkPos commandSource = new ChunkPos(new BlockPos(context.getSource().getPosition()));
        if(VillageIslandManager.INSTANCE.isIslandChunk(commandSource)){
            VillageIsland island = VillageIslandManager.INSTANCE.chunkToIsland(commandSource);


            String filename = context.getArgument("filename", String.class);
            context.getSource().sendFeedback(new TextComponent(String.format("Saving Island [%d:%d] as %s.", island.getBaseChunkPos().x, island.getBaseChunkPos().z, filename)), false);
            if(island.saveToFile(context.getSource().getWorld(), ModInfo.identifierFor(filename))) {
                context.getSource().sendFeedback(new TextComponent("Success!"), false);
            }else{
                context.getSource().sendFeedback(new TextComponent("Failure!"), false);
            }
        }else{
            context.getSource().sendFeedback(new TextComponent("Error: You are not in an island chunk!"), false);
        }
        return Command.SINGLE_SUCCESS;
    }


    public static int onLoadIsland(CommandContext<ServerCommandSource> context){
        ChunkPos commandSource = new ChunkPos(new BlockPos(context.getSource().getPosition()));
        if(VillageIslandManager.INSTANCE.isIslandChunk(commandSource)){
            VillageIsland island = VillageIslandManager.INSTANCE.chunkToIsland(commandSource);
            String filename = context.getArgument("filename", String.class);
            context.getSource().sendFeedback(new TextComponent(String.format("Loading Island [%d:%d] as %s.", island.getBaseChunkPos().x, island.getBaseChunkPos().z, filename)), false);
                if(island.loadFromFile(context.getSource().getWorld(), ModInfo.identifierFor(filename))) {
                    context.getSource().sendFeedback(new TextComponent("Success!"), false);
                }else{
                    context.getSource().sendFeedback(new TextComponent("Failure!"), false);
                }
        }else{
            context.getSource().sendFeedback(new TextComponent("Error: You are not in an island chunk!"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int onGotoIsland(CommandContext<ServerCommandSource> context){
        int x = context.getArgument("x", Integer.class) * (VillageIslandManager.ISLAND_CHUNK_SPACING + VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH);
        int y = context.getArgument("y", Integer.class) * (VillageIslandManager.ISLAND_CHUNK_SPACING + VillageIslandManager.ISLAND_MAX_CHUNK_WIDTH);

        try{
            VillageIsland targetIsland = VillageIslandManager.INSTANCE.chunkToIsland(new ChunkPos(x,y));
            BlockPos target = targetIsland.getBaseChunkPos().toBlockPos(targetIsland.getSpawnpoint().getX(), targetIsland.getSpawnpoint().getY(), targetIsland.getSpawnpoint().getZ());

            context.getSource().sendFeedback(new TextComponent("Teleporting to island at ["+x+":"+y+"]"), false);
            MovementUtility.teleportSmooth(context.getSource().getPlayer(), target, ((IServerPlayerEntityExtension)context.getSource().getPlayer()).getCameraDirection());
        }catch(Exception e) {
            context.getSource().sendFeedback(new TextComponent("Teleport Failed: " + e.getMessage()), false);
        }
        return Command.SINGLE_SUCCESS;

    }
}
