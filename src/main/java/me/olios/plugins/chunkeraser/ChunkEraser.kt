package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.commands.ChunkCommand
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class ChunkEraser : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        registerCommands()

        PluginManager.initialize(this)
        PluginManager.isEnabled()
    }

    override fun onDisable() {
        PluginManager.stopTimer()
    }

    private fun registerCommands() {
        getCommand("chunk")?.setExecutor(ChunkCommand(this))
    }
}