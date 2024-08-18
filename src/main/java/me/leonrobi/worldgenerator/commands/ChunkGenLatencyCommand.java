package me.leonrobi.worldgenerator.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ChunkGenLatencyCommand implements CommandExecutor {

    public static HashMap<World, Long> latencyMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 1 && !(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: /chunkgenlatency <world>");
            return false;
        }

        String worldName;

        if (args.length == 1) {
            worldName = args[0];
        } else {
            worldName = ((Player) commandSender).getWorld().getName();
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            commandSender.sendMessage(ChatColor.RED + "World '" + worldName + "' not found!");
            return false;
        }

        if (!latencyMap.containsKey(world)) {
            commandSender.sendMessage(ChatColor.RED + "No chunk generation latency data for world '" + worldName + "'! (Generate some chunks)");
            return false;
        }

        long latency = latencyMap.get(world);
        ChatColor color;
        if (latency < 40) {
            color = ChatColor.GREEN;
        } else if (latency < 80) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.RED;
        }

        commandSender.sendMessage("Chunk generation latency for '" + worldName + "': " + color + latency + "ms");
        return true;
    }
}
