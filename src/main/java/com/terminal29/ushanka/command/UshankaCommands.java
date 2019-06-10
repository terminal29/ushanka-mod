package com.terminal29.ushanka.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.terminal29.ushanka.dimension.VillageIsland;
import com.terminal29.ushanka.dimension.VillageIslandManager;
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
    }

    public static int onSaveIsland(CommandContext<ServerCommandSource> context){
        ChunkPos commandSource = new ChunkPos(new BlockPos(context.getSource().getPosition()));
        if(VillageIslandManager.INSTANCE.isIslandChunk(commandSource)){
            VillageIsland island = VillageIslandManager.INSTANCE.chunkToIsland(commandSource);


            String filename = context.getArgument("filename", String.class);
            context.getSource().sendFeedback(new TextComponent(String.format("Saving Island [%d:%d] as %s.", island.getIslandPos().getLeft(), island.getIslandPos().getRight(), filename)), false);
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
            context.getSource().sendFeedback(new TextComponent(String.format("Saving Island [%d:%d] as %s.", island.getBaseChunkPos().x, island.getBaseChunkPos().z, filename)), false);
        }else{
            context.getSource().sendFeedback(new TextComponent("Error: You are not in an island chunk!"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
