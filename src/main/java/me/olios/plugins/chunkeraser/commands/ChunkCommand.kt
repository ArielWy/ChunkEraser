package me.olios.plugins.chunkeraser.commands

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ChunkCommand(private val plugin: ChunkEraser): CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val command = p3?.getOrElse(0) { return false }?.lowercase()
        if (sender !is Player) return false

        when (command) {
            "run" -> PluginManager.removeChunkImmediately()

            "start" -> PluginManager.startTimer()
            "stop" -> PluginManager.stopTimer()
            "restart" -> PluginManager.restartTimer()
            "reload" -> {
                plugin.saveDefaultConfig()
                plugin.reloadConfig()
            }

            else -> return false
        }

        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String>? {
        if (p3?.size == 1) {
            // If the user is typing the first argument, filter the list based on the first character of p3[0]
            val options = mutableListOf("run", "start", "restart", "stop", "reload")
            return options.filter { it.startsWith(p3[0], ignoreCase = true) || p3[0].isEmpty() }.toMutableList()
        }
        return null
    }

}