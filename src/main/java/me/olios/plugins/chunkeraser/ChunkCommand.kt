package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.handlers.ChunkHandler
import me.olios.plugins.chunkeraser.handlers.TimerTask
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChunkCommand(private val plugin: ChunkEraser): CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val command = p3?.getOrElse(0) { return false }?.lowercase()
        if (sender !is Player) return false

        when (command) {
            "run" -> ChunkHandler(plugin).deleteRandomChunk()

            "start" -> PluginManager.startTimer()
            "stop" -> PluginManager.stopTimer()
            "restart" -> PluginManager.restartTimer()

            else -> return false
        }

        return true
    }
}