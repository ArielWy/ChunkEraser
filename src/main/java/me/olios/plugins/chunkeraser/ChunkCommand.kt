package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.handlers.ChunkHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChunkCommand(private val plugin: ChunkEraser): CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return false

        ChunkHandler(plugin).deleteRandomChunk()
        sender.sendMessage("chunkX: ${sender.chunk.x}, chunkZ: ${sender.chunk.z}")

        return true
    }
}