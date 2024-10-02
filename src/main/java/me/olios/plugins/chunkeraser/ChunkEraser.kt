package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.commands.ChunkCommand
import org.bukkit.plugin.java.JavaPlugin

class ChunkEraser : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        registerCommands()
    }

    private fun registerCommands() {
        getCommand("chunk")?.setExecutor(ChunkCommand(this))
    }
}